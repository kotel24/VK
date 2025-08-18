package ru.mygames.vk.domain.usecase

import kotlinx.coroutines.flow.StateFlow
import ru.mygames.vk.domain.entity.AuthState
import ru.mygames.vk.domain.entity.FeedPost
import ru.mygames.vk.domain.repository.NewsFeedRepository

class CheckAuthStateUseCase(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(){
        repository.checkAuthState()
    }
}