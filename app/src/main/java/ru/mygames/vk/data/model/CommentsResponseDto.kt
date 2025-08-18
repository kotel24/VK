package ru.mygames.vk.data.model

import com.google.gson.annotations.SerializedName

data class CommentsResponseDto(
    @SerializedName("response") val commentsContent: CommentsContentDto
)
