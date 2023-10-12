import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import org.jetbrains.skiko.wasm.onWasmReady
import org.ailingo.app.App
import org.ailingo.app.VoiceToTextParser

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        CanvasBasedWindow("ailingo") {
            val voiceToTextParser by lazy {
                VoiceToTextParser()
            }
            App(
                voiceToTextParser
            )
        }
    }
}
