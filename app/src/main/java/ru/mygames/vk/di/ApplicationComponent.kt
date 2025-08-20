package ru.mygames.vk.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.mygames.vk.MainActivity
import ru.mygames.vk.domain.entity.FeedPost

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance feedPost: FeedPost
        ): ApplicationComponent
    }
    fun inject(mainActivity: MainActivity)
}