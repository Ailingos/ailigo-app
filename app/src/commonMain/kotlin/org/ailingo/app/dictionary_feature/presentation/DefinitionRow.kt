package org.ailingo.app.dictionary_feature.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ailingo.app.dictionary_feature.data.Def
import org.ailingo.app.dictionary_feature.presentation.utils.getPartOfSpeechLabel


@Composable
fun DefinitionRow(definition: Def) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            definition.text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
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
