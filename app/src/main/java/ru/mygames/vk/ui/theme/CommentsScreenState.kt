package ru.mygames.vk.ui.theme

import ru.mygames.vk.domain.FeedPost
import ru.mygames.vk.domain.PostComment

sealed class CommentsScreenState {
    object Initial : CommentsScreenState()

    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>,
    ) : CommentsScreenState()
}