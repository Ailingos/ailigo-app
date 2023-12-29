package org.ailingo.app.feature_chat.presentation

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import org.ailingo.app.core.util.VoiceStates
import org.ailingo.app.core.util.VoiceToTextParser
import org.ailingo.app.feature_chat.data.model.Message

@Composable
actual fun CustomTextFieldForBottomChat(
    modifier: Modifier,
    voiceToTextParser: VoiceToTextParser,
    voiceState: State<VoiceStates>,
    messages: List<Message>,
    listState: LazyListState,
    chatViewModel: ChatViewModel,
    isActiveJob: Boolean,
    scope: CoroutineScope
) {
}