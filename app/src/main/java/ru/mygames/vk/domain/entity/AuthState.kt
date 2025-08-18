package ru.mygames.vk.domain.entity

sealed class AuthState {
    object Initial : AuthState()
    object Authorized : AuthState()
    object UnAuthorized : AuthState()
}