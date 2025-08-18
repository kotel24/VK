package ru.mygames.vk.domain

sealed class AuthState {
    object Initial : AuthState()
    object Authorized : AuthState()
    object UnAuthorized : AuthState()
}