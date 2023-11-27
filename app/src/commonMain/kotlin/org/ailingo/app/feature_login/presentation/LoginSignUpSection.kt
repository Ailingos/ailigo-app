package org.ailingo.app.feature_login.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import dev.icerock.moko.resources.compose.stringResource
import org.ailingo.app.SharedRes
import org.ailingo.app.core.util.VoiceToTextParser
import org.ailingo.app.feature_register.presentation.RegisterScreen

@Composable
fun LoginSignUpSection(navigator: Navigator, voiceToTextParser: VoiceToTextParser) {
    Row {
        Text(stringResource(SharedRes.strings.dont_have_an_account))
        Text(" ")
        Text(
            color = MaterialTheme.colorScheme.primary,
            text = stringResource(SharedRes.strings.sign_up),
            modifier = Modifier.clickable {
                navigator.push(RegisterScreen(voiceToTextParser))
            }
        )
    }
}