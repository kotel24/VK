package ru.mygames.vk.data.mapper

import ru.mygames.vk.data.model.CommentsResponseDto
import ru.mygames.vk.data.model.NewsFeedResponseDto
import ru.mygames.vk.domain.FeedPost
import ru.mygames.vk.domain.ItemInfo
import ru.mygames.vk.domain.PostComment
import ru.mygames.vk.domain.StatisticItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

class NewsFeedMapper {
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
    fun mapResponseToPosts(responseDto: NewsFeedResponseDto): List<FeedPost>{
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
                        type = ItemInfo.FAVORITE,
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
                    )
                ),
                isLiked = post.likes.userLikes > 0

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