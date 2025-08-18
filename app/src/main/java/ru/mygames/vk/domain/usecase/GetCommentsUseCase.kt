package ru.mygames.vk.domain.usecase

import kotlinx.coroutines.flow.StateFlow
import ru.mygames.vk.domain.entity.FeedPost
import ru.mygames.vk.domain.entity.PostComment
import ru.mygames.vk.domain.repository.NewsFeedRepository

class GetCommentsUseCase(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(feedPost: FeedPost): StateFlow<List<PostComment>>{
        return repository.getComments(feedPost = feedPost)
    }
}