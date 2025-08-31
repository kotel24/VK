package ru.mygames.vk.data.repository

import android.util.Log
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.mygames.vk.data.mapper.NewsFeedMapper
import ru.mygames.vk.data.network.ApiService
import ru.mygames.vk.domain.entity.AuthState
import ru.mygames.vk.domain.entity.FeedPost
import ru.mygames.vk.domain.entity.ItemInfo
import ru.mygames.vk.domain.entity.PostComment
import ru.mygames.vk.domain.entity.StatisticItem
import ru.mygames.vk.domain.repository.NewsFeedRepository
import ru.mygames.vk.extensions.mergeWith
import javax.inject.Inject

class NewsFeedRepositoryImpl @Inject constructor (
    private val apiService: ApiService,
    private val mapper: NewsFeedMapper,
    private val storage: VKPreferencesKeyValueStorage
) : NewsFeedRepository {


    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val token
        get() = VKAccessToken.restore(storage)

    // Authorization
    private val checkAuthStateEvents = MutableSharedFlow<Unit>(replay = 1)
    private val authStateFlow = flow {
        checkAuthStateEvents.emit(Unit)
        checkAuthStateEvents.collect {
            val currentToken = token
            Log.e("NewsFeedRepositoryImpl", "Favorite request with token = ${currentToken?.accessToken}")
            val loggedIn = currentToken != null && currentToken.isValid
            val authState = if (loggedIn) AuthState.Authorized else AuthState.UnAuthorized
            emit(authState)
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = AuthState.Initial
    )

    private fun getAccessToken(): String {
        return token?.accessToken ?: throw IllegalStateException("Token is null")
    }

    // Cached Recommendations
    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    // Recommendations
    private var nextFrom: String? = null

    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val refreshedListFlow = MutableSharedFlow<List<FeedPost>>()

    private val loadedListFlow = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            val startFrom = nextFrom
            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts)
                return@collect
            }
            val favoritesResponse = apiService.loadFavorite(getAccessToken())
            val favoriteIds = favoritesResponse.favoriteContent.items
                .map { it.post.id }
                .toSet()
            val response = if (startFrom == null) {
                apiService.loadRecommendation(getAccessToken())
            } else {
                apiService.loadRecommendation(getAccessToken(), startFrom)
            }
            nextFrom = response.newsFeedContent.nextFrom
            val posts = mapper.mapResponseToPosts(response, favoriteIds)
            _feedPosts.addAll(posts)
            emit(feedPosts)
        }
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }

    private val recommendations: StateFlow<List<FeedPost>> = loadedListFlow
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts
        )

    // Implementations

    override fun getAuthStateFlow(): StateFlow<AuthState> = authStateFlow

    override fun getRecommendations(): StateFlow<List<FeedPost>> = recommendations

    override fun getComments(feedPost: FeedPost): StateFlow<List<PostComment>> = flow {
        val comments = apiService.getComments(
            getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )
        emit(mapper.mapResponseToComments(comments))
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

    override suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
    }

    override suspend fun loadFavoriteData() {
        nextFavoritesNeededEvents.emit(Unit)
    }

    override suspend fun checkAuthState() {
        checkAuthStateEvents.emit(Unit)
    }

    override suspend fun deletePost(feedPost: FeedPost) {
        apiService.ignoreItem(
            getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )
        _feedPosts.remove(feedPost)
        refreshedListFlow.emit(feedPosts)
    }

    override suspend fun changeLikeStatus(feedPost: FeedPost) {
        val response = if (feedPost.isLiked) {
            apiService.deleteLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        } else {
            apiService.addLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        }
        val newLikesCount = response.likes.count
        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == ItemInfo.LIKE }
            add(StatisticItem(type = ItemInfo.LIKE, newLikesCount))
        }
        val newPost = feedPost.copy(statistics = newStatistics, isLiked = !feedPost.isLiked)
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
        refreshedListFlow.emit(feedPosts)
        refreshedFavoritesFlow.emit(feedPosts)
    }

    override suspend fun changeFavoriteStatus(feedPost: FeedPost) {
        if (feedPost.isFavorite == true) {
            apiService.removeFavorites(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        } else {
            apiService.addFavorites(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        }
        val newPost = feedPost.copy(isFavorite = !feedPost.isFavorite)
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
        refreshedListFlow.emit(feedPosts)

    }

    private val _favoritePosts = mutableListOf<FeedPost>()
    private val favoritePosts: List<FeedPost>
        get() = _favoritePosts.toList()

    private val nextFavoritesNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val refreshedFavoritesFlow = MutableSharedFlow<List<FeedPost>>()

    private val loadedFavoritesFlow = nextFavoritesNeededEvents
        .onStart { emit(Unit) } // ← автозапуск
        .map {
            val response = apiService.loadFavorite(getAccessToken())
            val posts = mapper.mapResponseToFavorites(response)
            _favoritePosts.clear()
            _favoritePosts.addAll(posts)
            favoritePosts
        }
        .retry {
            delay(RETRY_TIMEOUT_MILLIS)
            true
        }

    private val favorites: StateFlow<List<FeedPost>> = loadedFavoritesFlow
        .mergeWith(refreshedFavoritesFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = favoritePosts
        )

    override fun getFavorites(): StateFlow<List<FeedPost>> = favorites

    companion object {

        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}