package org.ailingo.app.feature_dictionary.presentation.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight


@Composable
fun HighlightedText(text: String, searchQuery: String?) {
    val lowercaseText = text.lowercase()
    val lowercaseQuery = searchQuery?.lowercase()

    val annotatedText = buildAnnotatedString {
        val startIndex = lowercaseText.indexOf(lowercaseQuery ?: "")
        val endIndex = startIndex + (lowercaseQuery?.length ?: 0)

        append(text)

        if (startIndex != -1 && endIndex != -1) {
            addStyle(
                style = SpanStyle(
                    color = Color.Green,
                    fontWeight = FontWeight.Bold
                ),
                start = startIndex,
                end = endIndex
            )
        }
    }

    Text(
        text = annotatedText,
    )
}