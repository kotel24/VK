package ru.mygames.vk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mygames.vk.domain.FeedPost
import ru.mygames.vk.domain.StatisticItem

class MainViewModel: ViewModel() {
    private val _feedPost = MutableLiveData(FeedPost())
    val feedPost: LiveData<FeedPost> = _feedPost

    fun updateCount(item: StatisticItem){
        val oldStatistic = feedPost.value?.statistics ?: throw IllegalStateException()
        val newStatistic = oldStatistic.toMutableList().apply {
            replaceAll{ oldItem ->
                if (oldItem.type == item.type){
                    oldItem.copy(count = oldItem.count + 1)
                }else {oldItem}
            }
        }
        _feedPost.value = feedPost.value?.copy(statistics = newStatistic)
    }
}