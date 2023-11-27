package org.ailingo.app.feature_login.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.stringResource
import org.ailingo.app.SharedRes

@Composable
fun LoginWelcomeMessage() {
    Text(
        text = stringResource(SharedRes.strings.welcome_back),
        style = MaterialTheme.typography.headlineLarge
    )
    Spacer(modifier = Modifier.height(8.dp))
}