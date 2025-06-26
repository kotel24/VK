package ru.mygames.vk.domain

import ru.mygames.vk.R

data class PostComment(
    val id: Int,
    val authorName: String = "Author",
    val avatarId: Int = R.drawable.baseline_account_circle_24,
    val commentText: String = "Long comment text",
    val publicationDate: String = "00:00"
)
