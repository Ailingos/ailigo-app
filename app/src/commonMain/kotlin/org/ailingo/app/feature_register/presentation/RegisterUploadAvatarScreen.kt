package org.ailingo.app.feature_register.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import org.ailingo.app.core.util.VoiceToTextParser
import org.ailingo.app.feature_chat.presentation.ChatScreen

data class RegisterUploadAvatarScreen(
    val login: MutableState<String>,
    val password: MutableState<String>,
    val email: MutableState<String>,
    val name: MutableState<String>,
    val voiceToTextParser: VoiceToTextParser,
    val savedPhoto: MutableState<String>
) : Screen {
    @Composable
    override fun Content() {
        val registerViewModel = getViewModel(Unit, viewModelFactory { RegistrationViewModel() })
        val registerState = registerViewModel.registerState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        when (registerState.value) {
            RegisterUiState.Empty -> {
                RegisterUploadAvatarEmpty(
                    navigator,
                    voiceToTextParser,
                    registerViewModel,
                    login,
                    password,
                    email,
                    name,
                    savedPhoto
                )
            }

            is RegisterUiState.Error -> {
                RegisterError(
                    errorMessage = (registerState.value as RegisterUiState.Error).message,
                    onBackToEmptyState = {
                        registerViewModel.backToEmptyState()
                        navigator.push(RegisterScreen(voiceToTextParser))
                    })
            }

            RegisterUiState.Loading -> {
                RegisterLoading()
            }

            is RegisterUiState.Success -> {
                navigator.push(ChatScreen(voiceToTextParser))
            }
        }
    }
}