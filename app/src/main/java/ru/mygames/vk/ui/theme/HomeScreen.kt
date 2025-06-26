package ru.mygames.vk.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.mygames.vk.NewsFeedViewModel
import ru.mygames.vk.domain.FeedPost

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onCommentClickListener: (FeedPost) -> Unit
){
    val viewModel: NewsFeedViewModel = viewModel()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val screenState = viewModel.screenState.observeAsState(NewsFeetScreenState.Initial)
        val currentState =screenState.value
        when(currentState){
            is NewsFeetScreenState.Posts ->{
                FeedPosts(viewModel = viewModel,
                    modifier = modifier,
                    posts = currentState.posts,
                    onCommentClickListener = onCommentClickListener
                )
            }
            NewsFeetScreenState.Initial -> {

            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
    viewModel: NewsFeedViewModel,
    modifier: Modifier = Modifier,
    posts: List<FeedPost>,
    onCommentClickListener: (FeedPost) -> Unit,
){
    LazyColumn (contentPadding = PaddingValues(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 108.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items ( items = posts, key = {it.id} ){
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
                    onCommentClickListener = onCommentClickListener
                )
            }
        }
    }
}