package org.ailingo.app.feature_login.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.stringResource
import org.ailingo.app.SharedRes

@Composable
fun LoginButton(
    onClick: () -> Unit,
    login: String,
    password: String,
    showLoginIsEmpty: MutableState<Boolean>,
    showPasswordIsEmpty: MutableState<Boolean>,
    isLoading: MutableState<Boolean>
) {
    Button(
        modifier = Modifier
            .width(OutlinedTextFieldDefaults.MinWidth)
            .height(OutlinedTextFieldDefaults.MinHeight),
        shape = MaterialTheme.shapes.small,
        onClick = {
            isLoading.value = true
            showLoginIsEmpty.value = login.isBlank()
            showPasswordIsEmpty.value = password.isBlank()

            if (login.isNotBlank() && password.isNotBlank()) {
                onClick()
            }
        },
    ) {
        Text(stringResource(SharedRes.strings.continue_app))
    }
    Spacer(modifier = Modifier.height(16.dp))
}