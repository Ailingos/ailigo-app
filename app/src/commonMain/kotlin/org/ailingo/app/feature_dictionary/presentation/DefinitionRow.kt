package org.ailingo.app.feature_dictionary.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.ailingo.app.feature_dictionary.data.model.Def
import org.ailingo.app.feature_dictionary.presentation.utils.getPartOfSpeechLabel


@Composable
fun DefinitionRow(definition: Def) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            definition.text,
            style = MaterialTheme.typography.titleLarge,
        )
        Text("[" + definition.ts + "]")
        Text(getPartOfSpeechLabel(definition.pos))
    }
    Spacer(modifier = Modifier.height(8.dp))

    // Handle tr elements and display them in a more modular way
    definition.tr.forEachIndexed { index, tr ->
        DefinitionEntry(index, tr)
    }
}
