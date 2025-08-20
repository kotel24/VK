package ru.mygames.vk.presentation.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.mygames.vk.data.repository.NewsFeedRepositoryImpl
import ru.mygames.vk.domain.repository.NewsFeedRepository
import ru.mygames.vk.domain.entity.FeedPost
import ru.mygames.vk.domain.usecase.ChangeLikeStatusUseCase
import ru.mygames.vk.domain.usecase.DeletePostUseCase
import ru.mygames.vk.domain.usecase.GetRecommendationsUseCase
import ru.mygames.vk.domain.usecase.LoadNextDataUseCase
import ru.mygames.vk.extensions.mergeWith
import javax.inject.Inject

class NewsFeedViewModel @Inject constructor(
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val loadNextDataUseCase: LoadNextDataUseCase,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase,
    private val deletePostUseCase: DeletePostUseCase,
) : ViewModel() {
    private val exceptionHandler = CoroutineExceptionHandler{_, _ ->
        Log.d("NewsFeedViewModel", "Error occurred in coroutine")
    }

    private val recommendationsFlow = getRecommendationsUseCase()
    private val loadNextDataFlow = MutableSharedFlow<NewsFeedScreenState>()
    val screenState = recommendationsFlow
        .filter { it.isNotEmpty() }
        .map { NewsFeedScreenState.Posts(posts = it) as NewsFeedScreenState }
        .onStart { emit(NewsFeedScreenState.Loading) }
        .mergeWith(loadNextDataFlow)

    fun loadNextRecommendations() {
        viewModelScope.launch {
            loadNextDataFlow.emit(
                NewsFeedScreenState.Posts(
                    posts = recommendationsFlow.value,
                    nextDataIsLoading = true
                )
            )
            loadNextRecommendations()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch (exceptionHandler) {
            changeLikeStatusUseCase(feedPost)
        }
    }


    fun delete(feedPost: FeedPost) {
        viewModelScope.launch (exceptionHandler){
            deletePostUseCase(feedPost)
        }
    }
}