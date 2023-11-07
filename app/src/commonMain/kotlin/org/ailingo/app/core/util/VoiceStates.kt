package org.ailingo.app.core.util

data class VoiceStates(
    var spokenText: String = "",
    val isSpeaking: Boolean = false,
    val error: String? = null
)