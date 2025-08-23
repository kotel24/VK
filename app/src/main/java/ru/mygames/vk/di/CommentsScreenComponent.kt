package ru.mygames.vk.di

import dagger.BindsInstance
import dagger.Subcomponent
import ru.mygames.vk.domain.entity.FeedPost
import ru.mygames.vk.presentation.ViewModelFactory

@Subcomponent(
    modules = [
        CommentsViewModelModule::class
    ]
)
interface CommentsScreenComponent {

    fun getViewModelFactory(): ViewModelFactory
    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance feedPost: FeedPost
        ): CommentsScreenComponent
    }
}