package org.ailingo.app.feature_login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import org.ailingo.app.core.util.VoiceToTextParser
import org.ailingo.app.feature_chat.presentation.ChatScreen

data class LoginScreen(val voiceToTextParser: VoiceToTextParser) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val loginViewModel = getViewModel(Unit, viewModelFactory { LoginViewModel() })
        val loginState = loginViewModel.loginState.collectAsState()
        var login by rememberSaveable {
            mutableStateOf("admin")
        }
        var password by rememberSaveable {
            mutableStateOf("pass")
        }
        when (loginState.value) {
            LoginUiState.Empty -> {
                LoginMainScreen(
                    navigator,
                    loginViewModel,
                    voiceToTextParser,
                    login = login,
                    onLoginChange = {
                        login = it
                    },
                    password = password,
                    onPasswordChange = {
                        password = it
                    }
                )
            }

            is LoginUiState.Error -> {
                LoginErrorScreen(loginViewModel, (loginState.value as LoginUiState.Error).message)
            }

            LoginUiState.Loading -> {
                LoginLoadingScreen()
            }

            is LoginUiState.Success -> {
                navigator.push(ChatScreen(voiceToTextParser))
            }
        }
    }
}