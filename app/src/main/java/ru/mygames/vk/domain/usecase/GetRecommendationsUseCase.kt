package ru.mygames.vk.domain.usecase

import kotlinx.coroutines.flow.StateFlow
import ru.mygames.vk.domain.entity.FeedPost
import ru.mygames.vk.domain.repository.NewsFeedRepository

class GetRecommendationsUseCase(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(): StateFlow<List<FeedPost>>{
        return repository.getRecommendations()
    }
}