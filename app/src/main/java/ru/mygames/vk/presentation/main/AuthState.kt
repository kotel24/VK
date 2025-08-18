package ru.mygames.vk.presentation.main

sealed class AuthState {
    object Initial : AuthState()
    object Authorized : AuthState()
    object UnAuthorized : AuthState()
}