package ru.mygames.vk.domain.entity

data class StatisticItem(
    val type: ItemInfo,
    val count: Int = 0
)
enum class ItemInfo{
    VIEW, LIKE, REPOST, COMMENT, FAVORITE
}
