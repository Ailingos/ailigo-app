package org.ailingo.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.ailingo.app.database.HistoryDictionaryDatabase
import org.w3c.dom.Audio
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.Worker
import org.w3c.files.File
import org.w3c.files.FileReader
import org.w3c.files.get
import kotlin.js.Promise

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

internal actual fun getPlatformName(): String {
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

actual class DriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        return WebWorkerDriver(
            Worker(
                js("""new URL("sqlite.worker.js", import.meta.url)""")
            )
        ).also { HistoryDictionaryDatabase.Schema.create(it).await() }
    }
}


actual suspend fun selectImage(): String? {
    val input = document.createElement("input") as HTMLInputElement
    input.type = "file"

    // Set accept attribute to limit file selection to images
    input.accept = "image/jpg"
    input.accept = "image/png"

    // Use Promise to handle asynchronous file reading
    val promise = Promise<String?> { resolve, _ ->
        input.onchange = { _ ->
            val file = input.files?.get(0)
            if (file != null) {
                readAsBase64(file) { base64String ->
                    resolve(base64String)
                }
            } else {
                resolve(null)
            }
        }
    }

    // Simulate a click on the input element to trigger file selection
    input.click()

    // Return the result as a nullable base64 string
    return promise.await()
}

private fun readAsBase64(file: File, callback: (String?) -> Unit) {
    val reader = FileReader()

    reader.onload = { _ ->
        val result = reader.result as? String
        val base64String = result?.substringAfter("base64,")
        callback(base64String)
    }

    reader.onerror = { _ ->
        callback(null)
    }

    // Read the file as a data URL (base64)
    reader.readAsDataURL(file)
}
