import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.ailingo.app.App
import org.ailingo.app.VoiceToTextParser
import java.awt.Dimension

fun main() = application {
    Window(
        title = "ailingo",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 600)

        val voiceToTextParser by lazy {
            VoiceToTextParser()
        }
        App(voiceToTextParser)
    }
}

@Preview
@Composable
fun previewDesktop() {
    val voiceToTextParser by lazy {
        VoiceToTextParser()
    }
    App(
        voiceToTextParser
    )
}