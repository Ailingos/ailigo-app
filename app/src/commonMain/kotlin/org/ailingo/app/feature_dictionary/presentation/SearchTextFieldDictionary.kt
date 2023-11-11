package org.ailingo.app.feature_dictionary.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Search


@Composable
fun SearchTextFieldDictionary(
    textFieldValue: String,
    onTextFieldValueChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    OutlinedTextField(
        value = textFieldValue,
        onValueChange = onTextFieldValueChange,
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        trailingIcon = {
            IconButton(onClick = onSearchClick) {
                Icon(FeatherIcons.Search, contentDescription = null)
            }
        },
        maxLines = 1,
        label = { Text("Search") }
    )
}