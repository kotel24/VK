package ru.mygames.vk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mygames.vk.domain.FeedPost
import ru.mygames.vk.domain.StatisticItem

class MainViewModel: ViewModel() {
    private val initialList = mutableListOf<FeedPost>().apply {
        repeat (3) {
            add(
                FeedPost(id = it)
            )
        }
    }

    private val _feedPost = MutableLiveData<List<FeedPost>>(initialList)
    val feedPost: LiveData<List<FeedPost>> = _feedPost

    fun updateCount(model: FeedPost, item: StatisticItem){
        val modifiedModel = feedPost.value?.toMutableList() ?: mutableListOf()
        modifiedModel.replaceAll{
            if (it.id == model.id){
                val newStatistic = model.statistics.toMutableList().apply {
                    replaceAll{ oldItem ->
                        if (oldItem.type == item.type){
                            oldItem.copy(count = oldItem.count + 1)
                        }else {oldItem}
                    }
                }
                it.copy(statistics = newStatistic)
            } else {it}
        }
        _feedPost.value = modifiedModel
    }

    fun delete (model: FeedPost){
        val modifiedModel = _feedPost.value?.toMutableList() ?: mutableListOf()
        modifiedModel.remove(model)
        _feedPost.value = modifiedModel
    }
}