package org.ailingo.app

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun GetStartedScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            stringResource(SharedRes.strings.get_started),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Spacer(modifier = Modifier.weight(1f))
            Button(modifier = Modifier.weight(1f), shape = MaterialTheme.shapes.small, onClick = {

            }) {
                Text(stringResource(SharedRes.strings.log_in))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(modifier = Modifier.weight(1f), shape = MaterialTheme.shapes.small, onClick = {

            }) {
                Text(stringResource(SharedRes.strings.sign_up))
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}