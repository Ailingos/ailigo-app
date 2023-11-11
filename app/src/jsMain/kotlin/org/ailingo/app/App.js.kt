package org.ailingo.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import kotlinx.browser.window
import org.w3c.dom.Audio

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

internal actual fun getPlatformName(): String{
    return "Web"
}

internal actual fun playSound(sound: String) {
    val audio = Audio(sound)
    audio.play()
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal actual fun getConfiguration(): Pair<Int, Int> {
    val containerSize = LocalWindowInfo.current.containerSize
    return Pair(containerSize.width, containerSize.height)
}