package ru.mygames.vk.presentation.news

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.mygames.vk.R
import ru.mygames.vk.domain.entity.FeedPost
import ru.mygames.vk.domain.entity.ItemInfo
import ru.mygames.vk.domain.entity.StatisticItem
import ru.mygames.vk.ui.theme.Black500

@Composable
fun PostCard (
    viewModel: NewsFeedViewModel,
    modifier: Modifier = Modifier,
    feedPost: FeedPost,
    onCommentClickListener: (FeedPost) -> Unit,
) {
    Card (
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(4.dp).copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
    ) {
        Item(modifier, feedPost)
        Spacer(modifier = Modifier.height(8.dp))
        Statistics(
            statistics = feedPost.statistics,
            onLikeClickListener = { viewModel.changeLikeStatus(feedPost)},
            onCommentClickListener = { onCommentClickListener(feedPost)},
            isFavorite = feedPost.isLiked)
        Spacer(modifier = Modifier.height(8.dp))
    }
}
@Composable
fun Item(
    modifier: Modifier = Modifier,
    feedPost: FeedPost
){
    Column {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically){
            AsyncImage(
                model = feedPost.communityImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
            Column (modifier = Modifier.weight(1f)) {
                Text( text = feedPost.communityName,)
                Spacer(modifier = Modifier.height(4.dp))
                Text( text = feedPost.publicationDate, color = Black500)
            }
            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = null,
                tint = Black500
            )
        }
        Text( text = feedPost.contentText, modifier = Modifier.padding(5.dp))
        AsyncImage(
            model = feedPost.contentImageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 8.dp, end = 8.dp),
            contentScale = ContentScale.FillWidth)
    }
}

@Composable
fun Statistics(
    statistics: List<StatisticItem>,
    onLikeClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit,
    isFavorite: Boolean
){
    Row {
        Row (modifier = Modifier
            .weight(1f)
            .padding(start = 8.dp)){
            val viewItem = statistics.getItemByType(ItemInfo.VIEW)
            IconWithText(
                iconResId = R.drawable.baseline_remove_red_eye_24,
                text = formatStatisticsCount(viewItem.count),)
        }
        Row (modifier = Modifier
            .weight(1f)
            .padding(end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            val shareItem = statistics.getItemByType(ItemInfo.REPOST)
            IconWithText(
                iconResId = R.drawable.baseline_share_24,
                text = formatStatisticsCount(shareItem.count),)
            val commentItem = statistics.getItemByType(ItemInfo.COMMENT)
            IconWithText(
                iconResId = R.drawable.baseline_comment_24,
                text = formatStatisticsCount(commentItem.count),
                onItemClickListener = {
                    onCommentClickListener(commentItem)
                })
            val favoriteItem = statistics.getItemByType(ItemInfo.FAVORITE)
            IconWithText(
                iconResId = if (isFavorite) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24,
                text = formatStatisticsCount(favoriteItem.count),
                onItemClickListener = {
                    onLikeClickListener(favoriteItem)
                }
            )
        }
    }
}

private fun formatStatisticsCount(count:Int): String{
    return if (count > 100_000) {
        String.format("%sK", (count / 1000))
    }
    else if (count > 1000) {
        String.format("%.1fk", (count / 1000f))
    }
    else {
        count.toString()
    }
}

private fun List<StatisticItem>.getItemByType(type: ItemInfo): StatisticItem{
    return this.find { it.type == type }?: throw IllegalStateException("dont find type Statistics")
}

@Composable
private fun IconWithText(
    iconResId: Int,
    text: String,
    onItemClickListener: (() -> Unit)? = null
){
    val modifier = if (onItemClickListener == null){
        Modifier
    } else {
        Modifier.clickable{onItemClickListener()}
    }
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painterResource(id = iconResId),
            contentDescription = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text)
    }
}