package org.ailingo.app.feature_chat.presentation

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import org.ailingo.app.core.helper_window_info.WindowInfo
import org.ailingo.app.core.helper_window_info.rememberWindowInfo
import org.ailingo.app.core.util.VoiceToTextParser

data class ChatScreen(val voiceToTextParser: VoiceToTextParser) : Screen {

    @Composable
    override fun Content() {
        val voiceState = voiceToTextParser.voiceState.collectAsState()
        val textField = remember {
            mutableStateOf("")
        }
        val chatViewModel = getViewModel(Unit, viewModelFactory { ChatViewModel() })
        val chatState = chatViewModel.chatState
        val isActiveJob = chatViewModel.isActiveJob.collectAsState(false)

        val listState = rememberLazyListState()
        var lastSpokenText by remember { mutableStateOf("") }

        LaunchedEffect(!voiceState.value.isSpeaking && voiceState.value.spokenText.isNotEmpty() && voiceState.value.spokenText != lastSpokenText) {
            textField.value = voiceState.value.spokenText
            lastSpokenText = voiceState.value.spokenText
        }

        val screenInfo = rememberWindowInfo()

        if (screenInfo.screenWidthInfo is WindowInfo.WindowType.DesktopWindowInfo) {
            ChatScreenDesktop(
                voiceToTextParser,
                textField,
                chatState,
                listState,
                voiceState,
                chatViewModel,
                isActiveJob
            )
        } else {
            ChatScreenMobile(
                voiceToTextParser,
                textField,
                chatState,
                listState,
                voiceState,
                chatViewModel,
                isActiveJob
            )
        }
    }
}
