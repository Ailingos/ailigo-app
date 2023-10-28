package org.ailingo.app

import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.speech.v1p1beta1.RecognitionAudio
import com.google.cloud.speech.v1p1beta1.RecognitionConfig
import com.google.cloud.speech.v1p1beta1.RecognizeRequest
import com.google.cloud.speech.v1p1beta1.SpeechClient
import com.google.cloud.speech.v1p1beta1.SpeechSettings
import com.google.protobuf.ByteString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.awt.Desktop
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
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

    private val speechClient: SpeechClient by lazy {
        val credentials =
            GoogleCredentials.fromStream(FileInputStream("C:\\Users\\vangel\\AndroidStudioProjects\\ailigo-app\\app\\src\\desktopMain\\resources\\chatappjson.json"))

        val speechSettings = SpeechSettings.newBuilder()
            .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
            .build()
        SpeechClient.create(speechSettings)
    }


    private var job: Job? = null
    actual fun startListening() {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {

            _voiceState.update {
                VoiceStates(isSpeaking = true)
            }
            val audioData = recordAudio(_voiceState)

            val recognitionConfig = RecognitionConfig.newBuilder()
                .setLanguageCode("ru-RU")
                .setSampleRateHertz(44100)
                .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16) // Set the correct encoding here
                .build()
            val audio = RecognitionAudio.newBuilder()
                .setContent(ByteString.copyFrom(audioData))
                .build()
            val request = RecognizeRequest.newBuilder()
                .setConfig(recognitionConfig)
                .setAudio(audio)
                .build()
            withContext(Dispatchers.Main) {
                val response = speechClient.recognize(request)

                val transcripts = response.resultsList.map { result ->
                    result.alternativesList.map { alternative ->
                        alternative.transcript
                    }.joinToString(" ") // Combine multiple alternatives if available
                }.joinToString(" ") // Combine results from different segments if available

                _voiceState.value = _voiceState.value.copy(
                    spokenText = transcripts,
                    isSpeaking = false,
                    error = null
                )

            }

        }
    }


    actual fun stopListening() {
        _voiceState.value = _voiceState.value.copy(isSpeaking = false)
    }
}

fun recordAudio(
    voiceState: MutableStateFlow<VoiceStates>
): ByteArray {
    val audioFormat = AudioFormat(44100f, 16, 1, true, false)

    val targetDataLineInfo = DataLine.Info(TargetDataLine::class.java, audioFormat)
    if (!AudioSystem.isLineSupported(targetDataLineInfo)) {
        println("TargetDataLine not supported")
        return ByteArray(0)
    }

    val targetDataLine = AudioSystem.getLine(targetDataLineInfo) as TargetDataLine
    targetDataLine.open(audioFormat)
    targetDataLine.start()

    val bufferSize = 4800
    val audioBuffer = ByteArray(bufferSize)

    var bytesRead = 0
    val byteArrayOutputStream = ByteArrayOutputStream()

    while (voiceState.value.isSpeaking || bytesRead == -1) {
        bytesRead = targetDataLine.read(audioBuffer, 0, audioBuffer.size)
        byteArrayOutputStream.write(audioBuffer, 0, bytesRead)
    }
    byteArrayOutputStream.close()
    targetDataLine.close()

    return byteArrayOutputStream.toByteArray()
}

