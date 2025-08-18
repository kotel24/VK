package ru.mygames.vk.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mygames.vk.data.repository.NewsFeedRepositoryImpl
import ru.mygames.vk.domain.repository.NewsFeedRepository
import ru.mygames.vk.domain.usecase.CheckAuthStateUseCase
import ru.mygames.vk.domain.usecase.GetAuthStateUseCase

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val repository = NewsFeedRepositoryImpl(application)

    private val getAuthStateUseCase = GetAuthStateUseCase(repository)
    private val checkAuthStateUseCase = CheckAuthStateUseCase(repository)

    val authState = getAuthStateUseCase()

    fun performAuthResult(){
        viewModelScope.launch {
            checkAuthStateUseCase()
        }
    }
}