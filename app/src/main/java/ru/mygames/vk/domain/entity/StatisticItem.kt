package ru.mygames.vk.domain.entity

data class StatisticItem(
    val type: ItemInfo,
    val count: Int = 0
)
enum class ItemInfo{
    VIEW, FAVORITE, REPOST, COMMENT
}
