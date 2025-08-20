package ru.mygames.vk.presentation

import android.app.Application
import ru.mygames.vk.di.ApplicationComponent
import ru.mygames.vk.di.DaggerApplicationComponent
import ru.mygames.vk.domain.entity.FeedPost

class NewsFeedApplication: Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(
            this,
            FeedPost(0,0,"","","","","", listOf(), false)
        )
    }
}