package org.ailingo.app.feature_chat.presentation.desktop

import ailingo.app.generated.resources.Res
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Mic
import compose.icons.feathericons.MicOff
import compose.icons.feathericons.Send
import kotlinx.coroutines.launch
import org.ailingo.app.CustomTextFieldImpl
import org.ailingo.app.core.helper_voice.VoiceStates
import org.ailingo.app.core.helper_voice.VoiceToTextParser
import org.ailingo.app.feature_chat.data.model.Message
import org.ailingo.app.feature_chat.presentation.ChatScreenComponent
import org.ailingo.app.feature_chat.presentation.ChatScreenEvents
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalResourceApi::class)
@Composable
fun BottomUserMessageBox(
    chatTextField: TextFieldValue,
    onChatTextField: (TextFieldValue) -> Unit,
    voiceToTextParser: VoiceToTextParser,
    voiceState: State<VoiceStates>,
    messages: List<Message>,
    listState: LazyListState,
    chatComponent: ChatScreenComponent,
    isActiveJob: Boolean,
) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        CustomTextFieldImpl(
            textValue = chatTextField,
            onValueChange = {
                onChatTextField(it)
            },
            modifier = Modifier.weight(4f),
            shape = RoundedCornerShape(18.dp),
            label = {
                Text(stringResource(Res.string.message))
            },
            maxLines = 5,
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
                            if (chatTextField.text.isNotBlank()) {
                                chatComponent.onEvent(ChatScreenEvents.MessageSent(chatTextField.text))
                                onChatTextField(TextFieldValue(""))
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
        Spacer(modifier = Modifier.weight(1f))
    }
}