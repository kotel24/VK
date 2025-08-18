package ru.mygames.vk.presentation.comments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mygames.vk.data.repository.NewsFeedRepository
import ru.mygames.vk.domain.FeedPost
import ru.mygames.vk.domain.PostComment
import ru.mygames.vk.presentation.news.NewsFeedScreenState

class CommentsViewModel(
    feedPost: FeedPost,
    application: Application
): ViewModel() {
    private val repository = NewsFeedRepository(application)

    val screenState = repository.getComments(feedPost)
        .map { CommentsScreenState.Comments(feedPost = feedPost, comments = it) as CommentsScreenState }


}