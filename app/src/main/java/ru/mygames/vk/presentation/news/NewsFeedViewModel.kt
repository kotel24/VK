package ru.mygames.vk.presentation.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.mygames.vk.data.repository.NewsFeedRepository
import ru.mygames.vk.domain.FeedPost
import ru.mygames.vk.extensions.mergeWith

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NewsFeedRepository(application)
    private val recommendationsFlow = repository.recommendations
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
            repository.loadNextData()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.changeLikeStatus(feedPost)
        }
    }


    fun delete(model: FeedPost) {
        viewModelScope.launch {
            repository.ignoreItem(model)
        }
    }
}