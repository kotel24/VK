package ru.mygames.vk.presentation.news

import ru.mygames.vk.domain.FeedPost

sealed class NewsFeedScreenState {
    object Initial : NewsFeedScreenState()

    object Loading : NewsFeedScreenState()
    data class Posts(
        val posts: List<FeedPost>,
        val nextDataIsLoading: Boolean = false
    ) : NewsFeedScreenState()
}