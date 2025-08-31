package ru.mygames.vk.data.model

import com.google.gson.annotations.SerializedName

data class FavoriteResponseDto (
    @SerializedName("response") val favoriteContent: FavoriteContentDto
)