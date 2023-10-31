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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.net.URI
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine
import kotlin.math.sqrt


internal actual fun openUrl(url: String?) {
    val uri = url?.let { URI.create(it) } ?: return
    Desktop.getDesktop().browse(uri)
}

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

fun recordAudio(
    voiceState: MutableStateFlow<VoiceStates>
): ByteArray {
    val audioFormat = AudioFormat(44100f, 16, 1, true, false)

    val targetDataLineInfo = DataLine.Info(TargetDataLine::class.java, audioFormat)
    if (!AudioSystem.isLineSupported(targetDataLineInfo)) {
        return ByteArray(0)
    }

    val targetDataLine = AudioSystem.getLine(targetDataLineInfo) as TargetDataLine
    targetDataLine.open(audioFormat)
    targetDataLine.start()

    val audioBuffer = ByteArray(4096)
    val byteArrayOutputStream = ByteArrayOutputStream()

    // Start recording
    targetDataLine.start()

    // Variable to keep track of silence counter
    var silenceCounter = 0

    // How much silence is needed before ending the recording (adjust as needed)
    val maxSilenceCounter = 50 // For example, 40 empty packets to end the recording

    while (voiceState.value.isSpeaking) {
        val bytesRead = targetDataLine.read(audioBuffer, 0, audioBuffer.size)

        // Analyze sound level in the current audio buffer
        val volume = calculateVolume(audioBuffer, bytesRead)

        if (volume > 0.02) { // Threshold value
            silenceCounter = 0 // Reset silence counter
        } else {
            silenceCounter++
        }

        if (silenceCounter >= maxSilenceCounter) {
            // Reached maximum silence count, stop recording
            break
        }

        byteArrayOutputStream.write(audioBuffer, 0, bytesRead)
    }

    voiceState.update {
        VoiceStates(spokenText = "Wait for transcripts...")
    }

    byteArrayOutputStream.close()
    targetDataLine.stop()
    targetDataLine.close()

    return byteArrayOutputStream.toByteArray()
}

// Function to calculate sound level in the buffer
fun calculateVolume(audioBuffer: ByteArray, bytesRead: Int): Double {
    var sum = 0.0
    for (i in 0 until bytesRead / 2) {
        val sample =
            (audioBuffer[2 * i].toInt() or (audioBuffer[2 * i + 1].toInt() shl 8)) / 32768.0
        sum += sample * sample
    }
    return sqrt(sum / (bytesRead / 2))
}