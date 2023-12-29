package org.ailingo.app.feature_chat.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Mic
import compose.icons.feathericons.MicOff
import compose.icons.feathericons.Send
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.ailingo.app.SharedRes
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
    var shortcutEvent: ShortcutEvent? by remember { mutableStateOf(null) }
    var isCtrlPressed by remember { mutableStateOf(false) }
    val textValue = remember {
        mutableStateOf(TextFieldValue())
    }
    val textHistory = remember {
        mutableListOf<TextFieldValue>()
    }

    LaunchedEffect(shortcutEvent) {
        ShortcutEventHandler(
            shortcutEvent = shortcutEvent,
            textValue = textValue,
            onValueChange = {
                textValue.value = it
            },
            textHistory
        )
        shortcutEvent = null
    }

    OutlinedTextField(
        value = textValue.value,
        onValueChange = {
            if (!isCtrlPressed) {
                textHistory.add(textValue.value)
                textValue.value = it
            }
        },
        modifier = modifier
            .onPreviewKeyEvent {
                isCtrlPressed = it.isCtrlPressed
                shortcutEvent = it.filterKeyDown()?.toShortcutEvent()
                false
            },
        shape = RoundedCornerShape(18.dp),
        label = {
            Text(stringResource(SharedRes.strings.message))
        },
        trailingIcon = {
            Row {
                IconButton(onClick = {
                    if (voiceState.value.isSpeaking) {
                        voiceToTextParser.stopListening()
                    } else {
                        voiceToTextParser.startListening()
                    }
                }) {
                    Icon(
                        imageVector = if (voiceState.value.isSpeaking) FeatherIcons.Mic else FeatherIcons.MicOff,
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                if (!isActiveJob) {
                    IconButton(onClick = {
                        if (textValue.value.text.isNotBlank()) {
                            chatViewModel.sendMessage(textValue.value.text)
                            textValue.value = TextFieldValue()
                            scope.launch {
                                listState.scrollToItem(messages.size - 1)
                            }
                        }
                    }) {
                        Icon(imageVector = FeatherIcons.Send, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
}

private fun undoLastChange(
    textHistory: MutableList<TextFieldValue>,
    textValue: MutableState<TextFieldValue>
) {
    if (textHistory.isNotEmpty()) {
        textValue.value = textHistory.removeAt(textHistory.size - 1)
    }
}

enum class ShortcutEvent {
    CUT, COPY, PASTE, HIGHLIGHT_ALL, UNDO
}

private suspend fun ShortcutEventHandler(
    shortcutEvent: ShortcutEvent?,
    textValue: MutableState<TextFieldValue>,
    onValueChange: (TextFieldValue) -> Unit,
    textHistory:  MutableList<TextFieldValue>
) {
    shortcutEvent ?: return
    when (shortcutEvent) {
        ShortcutEvent.CUT -> {
            onValueChange(textValue.value.replaceSelected(""))
        }

        ShortcutEvent.COPY -> {
            // Unused - seems to work out of the box
        }

        ShortcutEvent.PASTE -> {
            val clipboardText = window.navigator.clipboard.readText().await()
            val newText = textValue.value.text.replaceRange(textValue.value.selection.min, textValue.value.selection.max, clipboardText)
            onValueChange(
                textValue.value.copy(
                    text = newText,
                    selection = TextRange(
                        textValue.value.selection.min + clipboardText.length,
                        textValue.value.selection.min + clipboardText.length
                    )
                )
            )
        }

        ShortcutEvent.HIGHLIGHT_ALL -> {
            onValueChange(textValue.value.copy(selection = TextRange(0, textValue.value.text.length)))
        }

        ShortcutEvent.UNDO -> {
            undoLastChange(textHistory,textValue)
        }
    }
}

private fun KeyEvent.filterKeyDown() =
    if (type == KeyEventType.KeyDown) this else null

private fun KeyEvent.toShortcutEvent() = when {
    isCtrlPressed && key == Key.X -> ShortcutEvent.CUT
    isCtrlPressed && key == Key.V -> ShortcutEvent.PASTE
    isCtrlPressed && key == Key.A -> ShortcutEvent.HIGHLIGHT_ALL
    isCtrlPressed && key == Key.Z -> ShortcutEvent.UNDO
    else -> null
}

private fun TextFieldValue.replaceSelected(replacement: String) =
    copy(text = text.replaceRange(selection.min, selection.max, replacement), TextRange(0, 0))

