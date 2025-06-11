package ru.mygames.vk

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mygames.vk.domain.FeedPost
import ru.mygames.vk.domain.ItemInfo
import ru.mygames.vk.domain.StatisticItem
import ru.mygames.vk.ui.theme.Black500
import ru.mygames.vk.ui.theme.VKTheme
import java.sql.Types
import javax.xml.transform.ErrorListener

@Composable
fun PostCard (
    modifier: Modifier = Modifier,
    feedPost: FeedPost,
    onStatisticsItemClickListener: (StatisticItem) -> Unit
) {
    Card (
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(4.dp).copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
    ) {
        Item(modifier, feedPost)
        Spacer(modifier = Modifier.height(8.dp))
        Statistics(statistics = feedPost.statistics, onItemClickListener = onStatisticsItemClickListener)
        Spacer(modifier = Modifier.height(8.dp))
    }
}
@Composable
fun Item(
    modifier: Modifier = Modifier,
    feedPost: FeedPost
){
    Column {
        Row (modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically){
            Icon(painter = painterResource(id = feedPost.avatarResId),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
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
        Image(painter = painterResource(id = feedPost.contentImage),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp),
            contentScale = ContentScale.FillWidth)
    }
}

@Composable
fun Statistics(
    statistics: List<StatisticItem>,
    onItemClickListener: (StatisticItem)-> Unit
){
    Row {
        Row (modifier = Modifier.weight(1f).padding(start = 8.dp)){
            val viewItem = statistics.getItemByType(ItemInfo.VIEW)
            IconWithText(
                iconResId = R.drawable.baseline_remove_red_eye_24,
                text = viewItem.count.toString(),
                onItemClickListener = {
                    onItemClickListener(viewItem)
                })
        }
        Row (modifier = Modifier.weight(1f).padding(end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            val shareItem = statistics.getItemByType(ItemInfo.REPOST)
            IconWithText(
                iconResId = R.drawable.baseline_share_24,
                text = shareItem.count.toString(),
                onItemClickListener = {
                    onItemClickListener(shareItem)
                })
            val commentItem = statistics.getItemByType(ItemInfo.COMMENT)
            IconWithText(
                iconResId = R.drawable.baseline_comment_24,
                text = commentItem.count.toString(),
                onItemClickListener = {
                    onItemClickListener(commentItem)
                })
            val favoriteItem = statistics.getItemByType(ItemInfo.FAVORITE)
            IconWithText(
                iconResId = R.drawable.baseline_favorite_border_24,
                text = favoriteItem.count.toString(),
                onItemClickListener = {
                    onItemClickListener(favoriteItem)
                }
            )
        }
    }
}

private fun List<StatisticItem>.getItemByType(type: ItemInfo): StatisticItem{
    return this.find { it.type == type }?: throw IllegalStateException("dont find type Statistics")
}

@Composable
private fun IconWithText(
    iconResId: Int,
    text: String,
    onItemClickListener: () -> Unit
){
    Row (
        modifier = Modifier.clickable{
            onItemClickListener()
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painterResource(id = iconResId),
            contentDescription = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text)
    }
}