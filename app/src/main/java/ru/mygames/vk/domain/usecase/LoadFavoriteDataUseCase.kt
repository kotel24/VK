package ru.mygames.vk.domain.usecase

import ru.mygames.vk.domain.repository.NewsFeedRepository
import javax.inject.Inject

class LoadFavoriteDataUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(){
        repository.loadFavoriteData()
    }
}