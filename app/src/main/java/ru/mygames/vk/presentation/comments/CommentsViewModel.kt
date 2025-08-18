package ru.mygames.vk.presentation.comments

import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.map
import ru.mygames.vk.data.repository.NewsFeedRepositoryImpl
import ru.mygames.vk.domain.repository.NewsFeedRepository
import ru.mygames.vk.domain.entity.FeedPost
import ru.mygames.vk.domain.usecase.GetCommentsUseCase

class CommentsViewModel(
    feedPost: FeedPost,
    application: Application
): ViewModel() {
    private val repository = NewsFeedRepositoryImpl(application)

    private val getCommentsUseCase = GetCommentsUseCase(repository)

    val screenState = getCommentsUseCase(feedPost)
        .map { CommentsScreenState.Comments(feedPost = feedPost, comments = it) as CommentsScreenState }


}