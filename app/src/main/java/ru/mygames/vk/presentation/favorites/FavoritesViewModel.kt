package ru.mygames.vk.presentation.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.mygames.vk.domain.entity.FeedPost
import ru.mygames.vk.domain.usecase.ChangeFavoriteStatusUseCase
import ru.mygames.vk.domain.usecase.ChangeLikeStatusUseCase
import ru.mygames.vk.domain.usecase.DeletePostUseCase
import ru.mygames.vk.domain.usecase.GetFavoritesUseCase
import ru.mygames.vk.extensions.mergeWith
import ru.mygames.vk.presentation.news.NewsFeedScreenState
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val changeFavoriteStatusUseCase: ChangeFavoriteStatusUseCase,
) : ViewModel() {
    private val exceptionHandler = CoroutineExceptionHandler{_, _ ->
        Log.d("FavoritesViewModel", "Error occurred in coroutine")
    }

    private val favoritesFlow = getFavoritesUseCase()
    private val loadNextDataFlow = MutableSharedFlow<FavoritesScreenState>()
    val screenState = favoritesFlow
        .filter { it.isNotEmpty() }
        .map { FavoritesScreenState.Posts(posts = it) as FavoritesScreenState }
        .onStart { emit(FavoritesScreenState.Loading) }
        .mergeWith(loadNextDataFlow)


    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch (exceptionHandler) {
            changeLikeStatusUseCase(feedPost)
        }
    }

    fun loadNextRecommendations() {
        viewModelScope.launch {
            loadNextDataFlow.emit(
                FavoritesScreenState.Posts(
                    posts = favoritesFlow.value,
                    nextDataIsLoading = true
                )
            )
        }
    }

    fun changeFavoriteStatus(feedPost: FeedPost) {
        viewModelScope.launch (exceptionHandler) {
            changeFavoriteStatusUseCase(feedPost)
        }
    }

    fun delete(feedPost: FeedPost) {
        viewModelScope.launch (exceptionHandler){
            deletePostUseCase(feedPost)
        }
    }
}