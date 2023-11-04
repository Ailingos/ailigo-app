package org.ailingo.app.feature_dictionary.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.ailingo.app.feature_dictionary.data.model.Def

@Composable
fun DefinitionList(definitions: List<Def>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(definitions) { definition ->
            when (definition.pos) {
                "noun" -> {
                    DefinitionRow(definition)
                }

                "adjective" -> {
                    DefinitionRow(definition)
                }

                "verb" -> {
                    DefinitionRow(definition)
                }

                "foreign word" -> {
                    DefinitionRow(definition)
                }
            }
        }
    }
}