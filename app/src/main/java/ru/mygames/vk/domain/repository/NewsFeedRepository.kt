package ru.mygames.vk.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.mygames.vk.domain.entity.AuthState
import ru.mygames.vk.domain.entity.FeedPost
import ru.mygames.vk.domain.entity.PostComment

interface NewsFeedRepository {

    fun getAuthStateFlow(): StateFlow<AuthState>

    fun getRecommendations(): StateFlow<List<FeedPost>>

    fun getComments(feedPost: FeedPost): StateFlow<List<PostComment>>

    suspend fun loadNextData()

    suspend fun loadFavoriteData()

    suspend fun checkAuthState()

    suspend fun deletePost(feedPost: FeedPost)

    suspend fun changeLikeStatus(feedPost: FeedPost)

    suspend fun changeFavoriteStatus(feedPost: FeedPost)

    fun getFavorites(): StateFlow<List<FeedPost>>
}