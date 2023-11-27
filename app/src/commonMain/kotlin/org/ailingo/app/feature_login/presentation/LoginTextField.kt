package org.ailingo.app.feature_login.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.stringResource
import org.ailingo.app.SharedRes


@Composable
fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onNext: (KeyboardActionScope.() -> Unit),
    showErrorText: Boolean
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            label = { Text(text = stringResource(SharedRes.strings.login)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = onNext
            ),
            isError = showErrorText
        )
        if (showErrorText) {
            Text(
                text = stringResource(SharedRes.strings.login_cannot_be_empty),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }
    }
}