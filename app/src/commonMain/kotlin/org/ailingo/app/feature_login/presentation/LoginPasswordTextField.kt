package org.ailingo.app.feature_login.presentation

import ailingo.app.generated.resources.Res
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.ailingo.app.CustomTextFieldImpl
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalComposeUiApi::class, ExperimentalResourceApi::class)
@Composable
fun LoginPasswordTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibleChange: () -> Unit,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?,
    onLoginUser:() -> Unit,
    showErrorText: Boolean
) {
    Column {
        CustomTextFieldImpl(
            textValue = value,
            onValueChange = {
                onValueChange(it)
            },
            trailingIcon = {
                LoginPasswordVisibilityIcon(passwordVisible, onPasswordVisibleChange)
            },
            label = { Text(text = stringResource(Res.string.password)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onLoginUser()
                }
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.focusRequester(focusRequester),
            isError = showErrorText,
        )
        if (showErrorText) {
            Text(
                text = stringResource(Res.string.password_cannot_be_empty),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }
    }
}