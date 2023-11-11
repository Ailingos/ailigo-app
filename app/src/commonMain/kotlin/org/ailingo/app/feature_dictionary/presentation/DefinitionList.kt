package org.ailingo.app.feature_dictionary.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.stringResource
import org.ailingo.app.SharedRes
import org.ailingo.app.feature_dictionary.data.model.Def
import org.ailingo.app.feature_dictionary_examples.data.model.WordInfoItem

@Composable
fun DefinitionList(
    definitions: List<Def>,
    responseForExamples: List<WordInfoItem>?,
    textFieldValue: String
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(definitions) { definition ->
            when (definition.pos) {
                "noun" -> {
                    DefinitionRowInfo(definition, responseForExamples)
                }

                "adjective" -> {
                    DefinitionRowInfo(definition, responseForExamples)
                }

                "verb" -> {
                    DefinitionRowInfo(definition, responseForExamples)
                }

                "foreign word" -> {
                    DefinitionRowInfo(definition, responseForExamples)
                }
            }
        }
        item {
            Text(stringResource(SharedRes.strings.usage_examples), style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
        }
        item {
            ListOfExample(responseForExamples, textFieldValue)
        }
        item {
            Text(stringResource(SharedRes.strings.definitions), style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
        }
        item {
            ListOfDefinitions(responseForExamples)
        }
    }
}
