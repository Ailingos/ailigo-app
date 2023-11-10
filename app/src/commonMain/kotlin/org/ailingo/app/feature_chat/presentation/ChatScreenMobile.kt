package org.ailingo.app.feature_chat.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.ailingo.app.core.util.VoiceStates
import org.ailingo.app.core.util.VoiceToTextParser
import org.ailingo.app.feature_chat.data.model.Message

@Composable
fun ChatScreenMobile(
    voiceToTextParser: VoiceToTextParser,
    textField: MutableState<String>,
    chatState: List<Message>,
    listState: LazyListState,
    voiceState: State<VoiceStates>,
    chatViewModel: ChatViewModel,
    isActiveJob: State<Boolean>
) {

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Transparent
            ) {
                BottomUserMessageBoxMobile(
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
                MessageItemMobile(message)
            }
        }
    }
}