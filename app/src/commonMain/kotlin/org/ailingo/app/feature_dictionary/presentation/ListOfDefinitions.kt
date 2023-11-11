package org.ailingo.app.feature_dictionary.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.ailingo.app.feature_dictionary_examples.data.model.WordInfoItem

@Composable
fun ListOfDefinitions(responseForExamples: List<WordInfoItem>?) {
    val listOfDefinitions = responseForExamples?.flatMap {
        it.meanings.flatMap { meaning ->
            meaning.definitions.map { def ->
                def.definition
            }
        }
    }
    listOfDefinitions?.forEachIndexed { index, def ->
        val newIndex = index + 1
        Column {
            Row {
                Text(
                    text = newIndex.toString(),
                    modifier = Modifier.widthIn(min = 22.dp)
                )
                Text(
                    text = def,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}