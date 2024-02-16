package org.ailingo.app.feature_login.presentation

import ailingo.app.generated.resources.Res
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoginSignUpSection(
    onClick: () -> Unit
) {
    Row {
        Text(stringResource(Res.string.dont_have_an_account))
        Text(" ")
        Text(
            color = MaterialTheme.colorScheme.primary,
            text = stringResource(Res.string.sign_up),
            modifier = Modifier.clickable {
                onClick()
            }
        )
    }
}