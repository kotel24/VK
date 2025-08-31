package ru.mygames.vk.domain.usecase

import ru.mygames.vk.domain.entity.FeedPost
import ru.mygames.vk.domain.repository.NewsFeedRepository
import javax.inject.Inject

class ChangeFavoriteStatusUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(feedPost: FeedPost){
        repository.changeFavoriteStatus(feedPost)
    }
}