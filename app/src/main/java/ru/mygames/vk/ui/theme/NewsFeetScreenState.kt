package ru.mygames.vk.ui.theme

import ru.mygames.vk.domain.FeedPost

sealed class NewsFeetScreenState {
    object Initial : NewsFeetScreenState()

    data class Posts(
        val posts: List<FeedPost>,
    ) : NewsFeetScreenState()
}