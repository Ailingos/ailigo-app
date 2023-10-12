package org.ailingo.app

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.awt.Desktop
import java.net.URI
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine

internal actual fun openUrl(url: String?) {
    val uri = url?.let { URI.create(it) } ?: return
    Desktop.getDesktop().browse(uri)
}
actual class VoiceToTextParser {
    private val _voiceState = MutableStateFlow(VoiceStates())
    actual val voiceState = _voiceState.asStateFlow()

    private var targetDataLine: TargetDataLine? = null

    actual fun startListening() {
        val audioFormat = AudioFormat(16000f, 16, 1, true, true)
        val dataLineInfo = DataLine.Info(TargetDataLine::class.java, audioFormat)

        if (!AudioSystem.isLineSupported(dataLineInfo)) {
            _voiceState.value = _voiceState.value.copy(error = "Audio line not supported")
            return
        }

        targetDataLine = AudioSystem.getLine(dataLineInfo) as TargetDataLine
        targetDataLine?.open(audioFormat)
        targetDataLine?.start()

        val bufferSize = 4096
        val buffer = ByteArray(bufferSize)
        var bytesRead: Int

        _voiceState.value = _voiceState.value.copy(isSpeaking = true, error = null)

        Thread {
            while (_voiceState.value.isSpeaking) {
                bytesRead = targetDataLine?.read(buffer, 0, buffer.size) ?: 0
                if (bytesRead > 0) {
                    // Обработка данных из буфера
                }
            }

            targetDataLine?.stop()
            targetDataLine?.close()
        }.start()
    }

    actual fun stopListening() {
        targetDataLine?.stop()
        targetDataLine?.close()
        _voiceState.value = _voiceState.value.copy(isSpeaking = false)
    }
}