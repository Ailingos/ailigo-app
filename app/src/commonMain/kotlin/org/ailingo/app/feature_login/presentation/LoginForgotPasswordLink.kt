package org.ailingo.app.feature_login.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import dev.icerock.moko.resources.compose.stringResource
import org.ailingo.app.SharedRes


@Composable
fun LoginForgotPasswordLink(onClick: () -> Unit) {
    Box(
        modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            modifier = Modifier.clickable { onClick() },
            text = stringResource(SharedRes.strings.forgot_password),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.End
        )
    }
}