import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import org.ailingo.app.App
import org.ailingo.app.core.util.VoiceToTextParser
import org.jetbrains.skiko.wasm.onWasmReady

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
