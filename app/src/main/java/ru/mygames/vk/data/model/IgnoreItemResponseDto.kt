package ru.mygames.vk.data.model

import com.google.gson.annotations.SerializedName

data class IgnoreItemResponseDto (
    @SerializedName("response") val deletedItem: IgnoreItemDto
)