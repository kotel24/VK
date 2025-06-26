package ru.mygames.vk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mygames.vk.domain.FeedPost
import ru.mygames.vk.domain.StatisticItem
import ru.mygames.vk.ui.theme.NewsFeetScreenState

class NewsFeedViewModel : ViewModel() {
    private val initialList = mutableListOf<FeedPost>().apply {
        repeat(10) {
            add(
                FeedPost(id = it, contentText = "Content $it")
            )
        }
    }
    private val initialState: NewsFeetScreenState = NewsFeetScreenState.Posts(posts = initialList)

    private val _screenState = MutableLiveData<NewsFeetScreenState>(initialState)
    val screenState: LiveData<NewsFeetScreenState> = _screenState

    fun updateCount(model: FeedPost, item: StatisticItem) {
        val currentState = screenState.value
        if (currentState !is NewsFeetScreenState.Posts) return
        val modifiedModel = currentState.posts.toMutableList()
        modifiedModel.replaceAll {
            if (it.id == model.id) {
                val newStatistic = model.statistics.toMutableList().apply {
                    replaceAll { oldItem ->
                        if (oldItem.type == item.type) {
                            oldItem.copy(count = oldItem.count + 1)
                        } else {
                            oldItem
                        }
                    }
                }
                it.copy(statistics = newStatistic)
            } else {
                it
            }
        }
        _screenState.value = NewsFeetScreenState.Posts(posts = modifiedModel)
    }

    fun delete(model: FeedPost) {
        val currentState = screenState.value
        if (currentState !is NewsFeetScreenState.Posts) return
        val modifiedModel = currentState.posts.toMutableList()
        modifiedModel.remove(model)
        _screenState.value = NewsFeetScreenState.Posts(posts = modifiedModel)
    }
}