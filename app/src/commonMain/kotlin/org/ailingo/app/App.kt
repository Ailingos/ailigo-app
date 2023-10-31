package org.ailingo.app

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import org.ailingo.app.theme.AppTheme

@Composable
internal fun App(voiceToTextParser: VoiceToTextParser) {
    AppTheme {
        Navigator(GetStartedScreen(voiceToTextParser)) { navigator ->
            val topAppBarScreens = listOf(
                LoginScreen(voiceToTextParser).key,
                RegistrationScreen(voiceToTextParser).key,
                GetStartedScreen(voiceToTextParser).key,
                ResetPasswordScreen(voiceToTextParser).key
            )

            Scaffold(
                topBar = {
                    if (navigator.lastItem.key in topAppBarScreens) {
                        TopAppBarForStart()
                    }
                },
                content = {
                    CurrentScreen()
                }
            )
        }
        //Navigator(ChatScreen(voiceToTextParser))
        //RegistrationScreen()
        //LoginScreen()
        //ResetPasswordScreen()
    }
}


internal expect fun openUrl(url: String?)
