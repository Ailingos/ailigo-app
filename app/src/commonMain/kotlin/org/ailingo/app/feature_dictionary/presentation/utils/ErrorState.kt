package org.ailingo.app.feature_dictionary.presentation.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorState(errorMessage: String) {
    Text(errorMessage)
}