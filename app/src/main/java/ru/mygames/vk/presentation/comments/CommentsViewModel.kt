package ru.mygames.vk.presentation.comments

import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.map
import ru.mygames.vk.data.repository.NewsFeedRepositoryImpl
import ru.mygames.vk.domain.entity.FeedPost
import ru.mygames.vk.domain.usecase.GetCommentsUseCase
import javax.inject.Inject

class CommentsViewModel @Inject constructor(
    private val feedPost: FeedPost,
    private val getCommentsUseCase: GetCommentsUseCase
): ViewModel() {

    val screenState = getCommentsUseCase(feedPost)
        .map { CommentsScreenState.Comments(feedPost = feedPost, comments = it) as CommentsScreenState }


}