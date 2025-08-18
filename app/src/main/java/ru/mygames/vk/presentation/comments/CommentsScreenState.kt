package ru.mygames.vk.presentation.comments

import ru.mygames.vk.domain.entity.FeedPost
import ru.mygames.vk.domain.entity.PostComment

sealed class CommentsScreenState {
    object Initial : CommentsScreenState()

    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>,
    ) : CommentsScreenState()
}