package org.ailingo.app.core.util

import androidx.compose.ui.text.input.TextFieldValue

data class VoiceStates(
    var spokenText: TextFieldValue = TextFieldValue(""),
    val isSpeaking: Boolean = false,
    val error: String? = null
)