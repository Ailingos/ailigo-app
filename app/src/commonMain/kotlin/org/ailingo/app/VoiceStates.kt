package org.ailingo.app

data class VoiceStates(
    var spokenText: String = "",
    val isSpeaking: Boolean = false,
    val error: String? = null
)