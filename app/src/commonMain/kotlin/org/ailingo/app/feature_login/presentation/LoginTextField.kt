package org.ailingo.app.feature_login.presentation

import ailingo.app.generated.resources.Res
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.ailingo.app.CustomTextFieldImpl
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoginTextField(
    textValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onNext: (KeyboardActionScope.() -> Unit),
    showErrorText: Boolean
) {
    Column {
        CustomTextFieldImpl(
            textValue = textValue,
            onValueChange = {
                onValueChange(it)
            },
            label = { Text(text = stringResource(Res.string.login)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = onNext
            ),
            isError = showErrorText,
        )

        if (showErrorText) {
            Text(
                text = stringResource(Res.string.login_cannot_be_empty),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }
    }
}



