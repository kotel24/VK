package ru.mygames.vk.presentation.favorites

import ru.mygames.vk.domain.entity.FeedPost

sealed class FavoritesScreenState {
    object Initial : FavoritesScreenState()

    object Loading : FavoritesScreenState()
    data class Posts(
        val posts: List<FeedPost>,
        val nextDataIsLoading: Boolean = false
    ) : FavoritesScreenState()
}