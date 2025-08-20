package ru.mygames.vk.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mygames.vk.data.repository.NewsFeedRepositoryImpl
import ru.mygames.vk.domain.repository.NewsFeedRepository
import ru.mygames.vk.domain.usecase.CheckAuthStateUseCase
import ru.mygames.vk.domain.usecase.GetAuthStateUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val checkAuthStateUseCase: CheckAuthStateUseCase

): ViewModel() {
    val authState = getAuthStateUseCase()

    fun performAuthResult(){
        viewModelScope.launch {
            checkAuthStateUseCase()
        }
    }
}