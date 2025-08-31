package ru.mygames.vk.data.model

import com.google.gson.annotations.SerializedName

data class FavoriteDto(
    @SerializedName("type") val type: String,
    @SerializedName("seen") val seen: Boolean?,
    @SerializedName("added_date") val added_date: Int,
    @SerializedName("tags") val tags: List<String>?,
    @SerializedName("post") val post: PostDto,
)
