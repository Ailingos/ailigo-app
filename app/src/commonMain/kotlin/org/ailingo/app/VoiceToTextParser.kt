package org.ailingo.app

import kotlinx.coroutines.flow.StateFlow

expect class VoiceToTextParser() {
    val voiceState: StateFlow<VoiceStates>
    fun startListening()
    fun stopListening()
}
