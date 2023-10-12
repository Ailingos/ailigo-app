package org.ailingo.app

import kotlinx.browser.window
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal actual fun openUrl(url: String?) {
    url?.let { window.open(it) }
}

@JsModule("./voiceToText.js")
@JsNonModule
external object VoiceToText {
    fun startListening()
    fun stopListening()
    fun setRecognitionCallback(callback: (String) -> Unit)
    fun setListeningCallback(callback: (Boolean) -> Unit)
}

actual class VoiceToTextParser {
    private val _voiceState = MutableStateFlow(VoiceStates())
    actual val voiceState = _voiceState.asStateFlow()

    init {
        VoiceToText.setRecognitionCallback { recognizedText ->
            _voiceState.value = _voiceState.value.copy(spokenText = recognizedText)
        }
        VoiceToText.setListeningCallback { isSpeaking ->
            _voiceState.value = _voiceState.value.copy(isSpeaking = isSpeaking)
        }
    }

    actual fun startListening() {
        VoiceToText.startListening()
    }

    actual fun stopListening() {
        VoiceToText.stopListening()
    }
}