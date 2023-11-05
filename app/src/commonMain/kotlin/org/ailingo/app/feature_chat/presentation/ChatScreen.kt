package org.ailingo.app.feature_chat.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
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

        Scaffold(
            bottomBar = {
                BottomAppBar {
                    BottomUserMessageBox(
                        textField,
                        voiceToTextParser,
                        voiceState,
                        chatState,
                        listState,
                        chatViewModel,
                        isActiveJob.value
                    )
                }
            }
        ) { padding ->
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(chatState) { message ->
                    MessageItem(message)
                }
            }
        }
    }

}
