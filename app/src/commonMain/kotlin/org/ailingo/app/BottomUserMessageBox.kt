package org.ailingo.app

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Mic
import compose.icons.feathericons.MicOff
import compose.icons.feathericons.Send
import kotlinx.coroutines.launch


@Composable
fun BottomUserMessageBox(
    textField: MutableState<String>,
    voiceToTextParser: VoiceToTextParser,
    voiceState: State<VoiceStates>,
    messages: SnapshotStateList<Message>,
    listState: LazyListState
) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = textField.value,
            onValueChange = {
                textField.value = it
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
                    IconButton(onClick = {
                        if (textField.value.isNotBlank()) {
                            messages.add(Message(textField.value, isSentByUser = true))
                            textField.value = ""
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
        )
    }
}