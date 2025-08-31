package ru.mygames.vk.data.model

import com.google.gson.annotations.SerializedName

data class FavoriteContentDto(
    @SerializedName("count") val count: Int,
    @SerializedName("items") val items: List<FavoriteDto>,
    @SerializedName("profiles") val profiles: List<ProfileDto>?,
    @SerializedName("groups") val groups: List<GroupDto>?
)
