package ru.mygames.vk.presentation

import android.app.Application
import ru.mygames.vk.di.ApplicationComponent
import ru.mygames.vk.di.DaggerApplicationComponent

class NewsFeedApplication: Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(
            this
        )
    }
}