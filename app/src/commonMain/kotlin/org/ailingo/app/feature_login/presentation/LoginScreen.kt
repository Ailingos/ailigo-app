package org.ailingo.app.feature_login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.delay
import org.ailingo.app.core.util.VoiceToTextParser
import org.ailingo.app.feature_chat.presentation.ChatScreen

data class LoginScreen(val voiceToTextParser: VoiceToTextParser) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val loginViewModel = getViewModel(Unit, viewModelFactory { LoginViewModel() })
        val loginState = loginViewModel.loginState.collectAsState()
        var login by remember {
            mutableStateOf(TextFieldValue("admin"))
        }
        var password by rememberSaveable {
            mutableStateOf(TextFieldValue("pass"))
        }
        var passwordVisible by rememberSaveable {
            mutableStateOf(false)
        }
        val isLoading = remember {
            mutableStateOf(true)
        }
        LaunchedEffect(isLoading.value) {
            if (isLoading.value) {
                delay(500L) //just for cute ui
                isLoading.value = false
            }
        }

        when (loginState.value) {
            LoginUiState.Empty -> {
                LoginMainScreen(
                    navigator,
                    onLoginUser = {
                        loginViewModel.loginUser(login.text, password.text)
                    },
                    voiceToTextParser,
                    login = login,
                    onLoginChange = {
                        login = it
                    },
                    password = password,
                    onPasswordChange = {
                        password = it
                    },
                    passwordVisible,
                    onPasswordVisibleChange = {
                        passwordVisible = !passwordVisible
                    },
                    isLoading
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