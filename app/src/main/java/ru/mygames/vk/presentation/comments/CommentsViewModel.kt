package ru.mygames.vk.presentation.comments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mygames.vk.data.repository.NewsFeedRepository
import ru.mygames.vk.domain.FeedPost
import ru.mygames.vk.domain.PostComment
import ru.mygames.vk.presentation.news.NewsFeedScreenState

class CommentsViewModel(
    feedPost: FeedPost,
    application: Application
): ViewModel() {
    private val _screenState = MutableLiveData<CommentsScreenState>(CommentsScreenState.Initial)
    val screenState: LiveData<CommentsScreenState> = _screenState

    private val repository = NewsFeedRepository(application)
    init {
        loadComments(feedPost)
    }
    private fun loadComments(feedPost: FeedPost) {
        viewModelScope.launch {
            val comments = repository.getComments(feedPost)
            _screenState.value = CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = comments
            )
        }
    }
}