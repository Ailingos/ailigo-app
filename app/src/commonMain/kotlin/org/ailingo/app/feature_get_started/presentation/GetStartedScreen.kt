package org.ailingo.app.feature_get_started.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.stringResource
import org.ailingo.app.SharedRes

@Composable
fun GetStartedScreen(
    component: GetStartedScreenComponent
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            stringResource(SharedRes.strings.get_started),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Button(shape = MaterialTheme.shapes.small, onClick = {
                component.onEvent(GetStartedScreenEvent.OnNavigateToLoginScreen)
            }) {
                Text(stringResource(SharedRes.strings.log_in))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(shape = MaterialTheme.shapes.small, onClick = {
                component.onEvent(GetStartedScreenEvent.OnNavigateToRegisterScreen)
            }) {
                Text(stringResource(SharedRes.strings.sign_up))
            }
        }
    }
}