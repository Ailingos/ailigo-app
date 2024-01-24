package org.ailingo.app.feature_login.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.stringResource
import org.ailingo.app.SharedRes

@Composable
fun LoginSignUpSection(
    onClick: () -> Unit
) {
    Row {
        Text(stringResource(SharedRes.strings.dont_have_an_account))
        Text(" ")
        Text(
            color = MaterialTheme.colorScheme.primary,
            text = stringResource(SharedRes.strings.sign_up),
            modifier = Modifier.clickable {
                onClick()
            }
        )
    }
}