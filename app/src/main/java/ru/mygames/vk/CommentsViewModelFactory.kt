package ru.mygames.vk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mygames.vk.domain.FeedPost

class CommentsViewModelFactory(
    private val feedPost: FeedPost
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentsViewModel(feedPost) as T
    }

}