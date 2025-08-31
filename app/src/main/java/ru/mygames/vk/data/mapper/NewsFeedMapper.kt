package ru.mygames.vk.data.mapper

import ru.mygames.vk.data.model.CommentsResponseDto
import ru.mygames.vk.data.model.FavoriteResponseDto
import ru.mygames.vk.data.model.NewsFeedResponseDto
import ru.mygames.vk.domain.entity.FeedPost
import ru.mygames.vk.domain.entity.ItemInfo
import ru.mygames.vk.domain.entity.PostComment
import ru.mygames.vk.domain.entity.StatisticItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.absoluteValue

class NewsFeedMapper @Inject constructor(){
    fun mapResponseToComments(responseDto: CommentsResponseDto): List<PostComment> {
        val result = mutableListOf<PostComment>()

        val comments = responseDto.commentsContent.itemsComment
        val profiles = responseDto.commentsContent.profiles

        for (comment in comments){
            if (comment.text.isBlank()) continue
            val author = profiles.firstOrNull{it.id == comment.fromId} ?: continue
            val postComment = PostComment(
                id = comment.id,
                authorName = "${author.firstName} ${author.lastName}",
                authorAvatarUrl = author.avatarUrl,
                commentText = comment.text,
                publicationDate = refactorDate(comment.date.toLong())
            )
            result.add(postComment)
        }
        return result
    }

    fun mapResponseToPosts(
        responseDto: NewsFeedResponseDto,
        favoriteIds: Set<Long>,
    ): List<FeedPost>{
        val result = mutableListOf<FeedPost>()

        val posts = responseDto.newsFeedContent.posts
        val groups = responseDto.newsFeedContent.groups

        for (post in posts){
            val group = groups.find { it.id == post.communityId.absoluteValue } ?: break
            val feedPost = FeedPost(
                id = post.id,
                communityId = post.communityId,
                communityName = group.name,
                publicationDate = refactorDate(post.date),
                communityImageUrl = group.imageUrl,
                contentText = post.text,
                contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                statistics = listOf(
                    StatisticItem(
                        type = ItemInfo.LIKE,
                        count = post.likes.count,
                    ),
                    StatisticItem(
                        type = ItemInfo.COMMENT,
                        count = post.comments.count
                    ),
                    StatisticItem(
                        type = ItemInfo.REPOST,
                        count = post.reposts.count
                    ),
                    StatisticItem(
                        type = ItemInfo.VIEW,
                        count = post.views.count
                    ),
                    StatisticItem(
                        type = ItemInfo.FAVORITE,
                )
                ),
                isLiked = post.likes.userLikes > 0,
                isFavorite = favoriteIds.contains(post.id) == true
            )
            result.add(feedPost)
        }
        return result
    }

    fun mapResponseToFavorites(
        responseDto: FavoriteResponseDto
    ): List<FeedPost> {
        val result = mutableListOf<FeedPost>()

        val posts = responseDto.favoriteContent.items.map { it.post }

        for (post in posts) {
            val feedPost = FeedPost(
                id = post.id,
                communityId = post.communityId, // тут owner_id
                communityName = "", // пока пусто, т.к. groups нет
                publicationDate = refactorDate(post.date),
                communityImageUrl = "", // тоже пока null
                contentText = post.text,
                contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                statistics = listOf(
                    StatisticItem(ItemInfo.LIKE, post.likes?.count ?: 0),
                    StatisticItem(ItemInfo.COMMENT, post.comments?.count ?: 0),
                    StatisticItem(ItemInfo.REPOST, post.reposts?.count ?: 0),
                    StatisticItem(ItemInfo.VIEW, post.views?.count ?: 0),
                    StatisticItem(ItemInfo.FAVORITE, 0)
                ),
                isLiked = post.likes?.userLikes ?: 0 > 0,
                isFavorite = true
            )
            result.add(feedPost)
        }
        return result
    }

    private fun refactorDate(timestamp: Long): String{
        val date = Date(timestamp * 1000)
        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }
}