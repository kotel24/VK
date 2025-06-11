package ru.mygames.vk.domain

import ru.mygames.vk.R

data class FeedPost(
    val communityName: String = "уволено",
    val publicationDate: String = "14:00",
    val avatarResId: Int = R.drawable.baseline_account_circle_24,
    val contentText: String = "кабаныч, когда узнал, что если сотрудникам не платить они начинают умирать с голоду",
    val contentImage: Int = R.drawable.post_content_image,
    val statistics: List<StatisticItem> = listOf(
        StatisticItem(type = ItemInfo.VIEW, 966),
        StatisticItem(type = ItemInfo.REPOST, 7),
        StatisticItem(type = ItemInfo.COMMENT, 8),
        StatisticItem(type = ItemInfo.FAVORITE, 27)
    )
)
