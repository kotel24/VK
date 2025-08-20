package ru.mygames.vk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import ru.mygames.vk.domain.entity.AuthState
import ru.mygames.vk.presentation.NewsFeedApplication
import ru.mygames.vk.presentation.ViewModelFactory
import ru.mygames.vk.presentation.main.LoginScreen
import ru.mygames.vk.presentation.main.MainScreen
import ru.mygames.vk.presentation.main.MainViewModel
import ru.mygames.vk.ui.theme.VKTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as NewsFeedApplication)
            .component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            VKTheme {
                val viewModel: MainViewModel = viewModel(factory = viewModelFactory)
                val authState = viewModel.authState.collectAsState(AuthState.Initial)
                val launcher = rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract()
                ) {
                    viewModel.performAuthResult()
                }
                when (authState.value) {
                    is AuthState.Authorized -> {
                        MainScreen(viewModelFactory)
                    }

                    is AuthState.UnAuthorized -> {
                        LoginScreen { launcher.launch(arrayListOf(VKScope.WALL, VKScope.FRIENDS)) }
                    }

                    else -> {}
                }
            }
        }
    }
}