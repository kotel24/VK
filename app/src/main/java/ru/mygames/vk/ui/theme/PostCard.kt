package ru.mygames.vk.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mygames.vk.MainViewModel
import ru.mygames.vk.R
import ru.mygames.vk.domain.FeedPost
import ru.mygames.vk.domain.ItemInfo
import ru.mygames.vk.domain.StatisticItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PostCardList(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val model = viewModel.feedPost.observeAsState(listOf())
        LazyColumn (contentPadding = PaddingValues(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 108.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items ( model.value, key = {it.id} ){
                val dismissState = rememberSwipeToDismissBoxState()
                if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart){
                    viewModel.delete(it)
                }
                SwipeToDismissBox(
                    modifier = Modifier.animateItemPlacement(),
                    state = dismissState,
                    enableDismissFromStartToEnd = false,
                    backgroundContent = {
                        Box(
                            contentAlignment = Alignment.CenterEnd,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize()
                                .background(Color.Red.copy(alpha = 0.5f))
                        ){
                            Text(
                                text = "Delete item",
                                color = Color.White,
                                fontSize = 24.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    },
                ){
                    PostCard(viewModel = viewModel,
                        modifier = modifier,
                        feedPost = it,
                        )
                }
            }
        }
    }
}
@Composable
fun PostCard (
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    feedPost: FeedPost,
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
            onLikeClickListener = { viewModel.updateCount(feedPost, it)},
            onViewClickListener = { viewModel.updateCount(feedPost, it)},
            onShareClickListener = { viewModel.updateCount(feedPost, it)},
            onCommentClickListener = { viewModel.updateCount(feedPost, it)})
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
            Icon(painter = painterResource(id = feedPost.avatarResId),
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
        Image(painter = painterResource(id = feedPost.contentImage),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            contentScale = ContentScale.FillWidth)
    }
}

@Composable
fun Statistics(
    statistics: List<StatisticItem>,
    onLikeClickListener: (StatisticItem) -> Unit,
    onViewClickListener: (StatisticItem) -> Unit,
    onShareClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit
){
    Row {
        Row (modifier = Modifier
            .weight(1f)
            .padding(start = 8.dp)){
            val viewItem = statistics.getItemByType(ItemInfo.VIEW)
            IconWithText(
                iconResId = R.drawable.baseline_remove_red_eye_24,
                text = viewItem.count.toString(),
                onItemClickListener = {
                    onViewClickListener(viewItem)
                })
        }
        Row (modifier = Modifier
            .weight(1f)
            .padding(end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            val shareItem = statistics.getItemByType(ItemInfo.REPOST)
            IconWithText(
                iconResId = R.drawable.baseline_share_24,
                text = shareItem.count.toString(),
                onItemClickListener = {
                    onShareClickListener(shareItem)
                })
            val commentItem = statistics.getItemByType(ItemInfo.COMMENT)
            IconWithText(
                iconResId = R.drawable.baseline_comment_24,
                text = commentItem.count.toString(),
                onItemClickListener = {
                    onCommentClickListener(commentItem)
                })
            val favoriteItem = statistics.getItemByType(ItemInfo.FAVORITE)
            IconWithText(
                iconResId = R.drawable.baseline_favorite_border_24,
                text = favoriteItem.count.toString(),
                onItemClickListener = {
                    onLikeClickListener(favoriteItem)
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