package ru.mygames.vk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import ru.mygames.vk.ui.theme.ActivityResultTest
import ru.mygames.vk.ui.theme.MainScreen
import ru.mygames.vk.ui.theme.VKTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VKTheme {
                ActivityResultTest()
            }
        }
    }
}