package ru.mygames.vk.data.model

import com.google.gson.annotations.SerializedName

data class CommentsContentDto(
    @SerializedName("items") val itemsComment: List<CommentDto>,
    @SerializedName("profiles") val profiles: List<ProfileDto>
)
