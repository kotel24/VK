package ru.mygames.vk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import ru.mygames.vk.presentation.main.AuthState
import ru.mygames.vk.presentation.main.LoginScreen
import ru.mygames.vk.presentation.main.MainScreen
import ru.mygames.vk.presentation.main.MainViewModel
import ru.mygames.vk.ui.theme.VKTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VKTheme {
                val viewModel: MainViewModel = viewModel()
                val authState = viewModel.authState.observeAsState(AuthState.Initial)
                val launcher = rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract()
                ) { result ->
                    viewModel.performAuthResult(result)
                }
                when (authState.value) {
                    is AuthState.Authorized -> {
                        MainScreen()
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