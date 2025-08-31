package ru.mygames.vk.domain.entity

import androidx.compose.runtime.Immutable

@Immutable
data class FeedPost(
    val id: Long,
    val communityId: Long,
    val communityName: String,
    val publicationDate: String,
    val communityImageUrl: String,
    val contentText: String,
    val contentImageUrl: String?,
    val statistics: List<StatisticItem>,
    val isLiked: Boolean,
    val isFavorite: Boolean,
)
