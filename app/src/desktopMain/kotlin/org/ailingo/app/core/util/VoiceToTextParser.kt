package org.ailingo.app.core.util

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ailingo.app.recordAudio
import java.io.FileInputStream

actual class VoiceToTextParser {
    private val _voiceState = MutableStateFlow(VoiceStates())
    actual val voiceState = _voiceState.asStateFlow()

    private val speechClient: SpeechClient by lazy {
        val credentials =
            GoogleCredentials.fromStream(FileInputStream("src/desktopMain/resources/chatappjson.json"))

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
                .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                .build()
            val audio = RecognitionAudio.newBuilder()
                .setContent(ByteString.copyFrom(audioData))
                .build()
            val request = RecognizeRequest.newBuilder()
                .setConfig(recognitionConfig)
                .setAudio(audio)
                .build()

            val response = speechClient.recognize(request)

            val transcripts = response.resultsList.joinToString(" ") { result ->
                result.alternativesList.joinToString(" ") { alternative ->
                    alternative.transcript
                }
            }

            if (transcripts.isNotBlank()) {
                _voiceState.update {
                    VoiceStates(
                        spokenText = transcripts,
                        isSpeaking = false,
                        error = null
                    )
                }
            } else {
                _voiceState.update {
                    VoiceStates(
                        spokenText = "Empty message, please check your microphone",
                        isSpeaking = false,
                        error = null
                    )
                }
                delay(2000L)
                _voiceState.update {
                    VoiceStates(
                        spokenText = " ",
                        isSpeaking = false,
                        error = null
                    )
                }
            }

        }
    }

    actual fun stopListening() {
        _voiceState.value = _voiceState.value.copy(isSpeaking = false)
    }
}