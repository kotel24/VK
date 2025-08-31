package ru.mygames.vk.presentation.favorites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.mygames.vk.domain.entity.FeedPost
import ru.mygames.vk.presentation.getApplicationComponent
import ru.mygames.vk.presentation.news.PostCard
import ru.mygames.vk.ui.theme.DarkBlue

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    onCommentClickListener: (FeedPost) -> Unit
) {
    val component = getApplicationComponent()
    val viewModel: FavoritesViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.screenState.collectAsState(FavoritesScreenState.Initial)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        FavoritesScreenStateComponent(
            screenState = screenState,
            modifier = modifier,
            onCommentClickListener = onCommentClickListener,
            viewModel = viewModel
        )
    }
}

@Composable
private fun FavoritesScreenStateComponent(
    screenState: State<FavoritesScreenState>,
    onCommentClickListener: (FeedPost) -> Unit,
    viewModel: FavoritesViewModel,
    modifier: Modifier
) {
    when (val currentState = screenState.value) {
        is FavoritesScreenState.Posts -> {
            FavoritePosts(
                viewModel = viewModel,
                modifier = modifier,
                posts = currentState.posts,
                onCommentClickListener = onCommentClickListener,
                nextDataIsLoading = currentState.nextDataIsLoading
            )
        }
        FavoritesScreenState.Initial -> Unit
        FavoritesScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = DarkBlue)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun FavoritePosts(
    viewModel: FavoritesViewModel,
    modifier: Modifier = Modifier,
    posts: List<FeedPost>,
    onCommentClickListener: (FeedPost) -> Unit,
    nextDataIsLoading: Boolean
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = posts, key = { it.id }) { post ->
            val dismissState = rememberSwipeToDismissBoxState()
            if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                viewModel.delete(post)
            }
            SwipeToDismissBox(
                state = dismissState,
                enableDismissFromStartToEnd = false,
                backgroundContent = {
                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "Delete item",
                            color = Color.White,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            ) {
                PostCard(
                    modifier = modifier,
                    feedPost = post,
                    onCommentClick = onCommentClickListener,
                    onFavoriteClick = { post -> viewModel.changeFavoriteStatus(post) },
                    onLikeClick = { post -> viewModel.changeLikeStatus(post) }
                )
            }
        }
        item {
            if (nextDataIsLoading){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(color = DarkBlue)
                }
            } else {
                SideEffect {
                    viewModel.loadNextRecommendations()
                }
            }
        }
    }
}