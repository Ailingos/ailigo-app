package org.ailingo.app.feature_dictionary.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Volume1
import dev.icerock.moko.resources.compose.fontFamilyResource
import org.ailingo.app.SharedRes
import org.ailingo.app.feature_dictionary.data.model.Def
import org.ailingo.app.feature_dictionary.presentation.utils.getPartOfSpeechLabel
import org.ailingo.app.feature_dictionary_examples.data.model.WordInfoItem
import org.ailingo.app.playSound


@Composable
fun DefinitionRowInfo(definition: Def, responseForExamples: List<WordInfoItem>?) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            definition.text,
            style = MaterialTheme.typography.titleLarge,
        )
        val fontFamilyForTranscription: FontFamily =
            fontFamilyResource(SharedRes.fonts.NotoSans.light)
        Text("[" + definition.ts + "]", fontFamily = fontFamilyForTranscription, fontSize = 16.sp)
        Text(getPartOfSpeechLabel(definition.pos))
        val listOfAllAudio = responseForExamples?.flatMap { wordInfoItem ->
            wordInfoItem.phonetics
                .mapNotNull {
                    it.audio.takeIf { audio ->
                        audio?.isNotBlank() ?: false
                    }
                }
        }
        val firstNonEmptyAudio = listOfAllAudio?.firstOrNull()
        if (firstNonEmptyAudio != null) {
            IconButton(onClick = {
                playSound(firstNonEmptyAudio)
            }) {
                Icon(imageVector = FeatherIcons.Volume1, contentDescription = null)
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))

    definition.tr.forEachIndexed { index, tr ->
        DefinitionEntry(index, tr)
    }
}
