package org.ailingo.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Mic
import compose.icons.feathericons.MicOff
import compose.icons.feathericons.Send

@Composable
internal fun App(voiceToTextParser: VoiceToTextParser) {
    ChatScreen(voiceToTextParser)
}


@Composable
fun ChatScreen(voiceToTextParser: VoiceToTextParser) {
    val state = voiceToTextParser.voiceState.collectAsState()

    Scaffold() { padding ->
        Row(
            modifier = Modifier.fillMaxSize()
                .padding(padding)
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.value.spokenText,
                onValueChange = {
//                    voice.lastText.value
                },
                trailingIcon = {
                    Row {
                        IconButton(onClick = {
                            if (state.value.isSpeaking) {
                                voiceToTextParser.stopListening()
                            } else {
                                voiceToTextParser.startListening()
                            }
                        }) {
                            Icon(
                                imageVector = if (state.value.isSpeaking) FeatherIcons.Mic else FeatherIcons.MicOff,
                                contentDescription = null
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = {

                        }) {
                            Icon(imageVector = FeatherIcons.Send, contentDescription = null)
                        }
                    }
                }
            )
        }
    }
}

internal expect fun openUrl(url: String?)




