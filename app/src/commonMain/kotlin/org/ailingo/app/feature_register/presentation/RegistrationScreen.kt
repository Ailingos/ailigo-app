package org.ailingo.app.feature_register.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import org.ailingo.app.core.util.VoiceToTextParser
import org.ailingo.app.feature_chat.presentation.ChatScreen


data class RegistrationScreen(val voiceToTextParser: VoiceToTextParser) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val registerViewModel = getViewModel(Unit, viewModelFactory { RegistrationViewModel() })
        val registerState = registerViewModel.registerState.collectAsState()
        when (registerState.value) {
            RegisterUiState.Empty -> {
                RegisterStart(registerViewModel, navigator, voiceToTextParser)
            }

            is RegisterUiState.Error -> {
                RegisterError((registerState.value as RegisterUiState.Error).message, registerViewModel)
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