package org.ailingo.app.dictionary_feature.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ailingo.app.dictionary_feature.data.Tr
import org.ailingo.app.theme.ColorForMainTextDictionary
import org.ailingo.app.theme.ColorForSynonymsDictionary


@Composable
fun DefinitionEntry(index: Int, tr: Tr) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text((index + 1).toString(), modifier = Modifier.padding(bottom = 4.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(color = ColorForMainTextDictionary)
        ) {
            DefinitionText(tr.text, tr.gen)
        }
        tr.syn?.forEach { syn ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = ColorForSynonymsDictionary)
            ) {
                DefinitionText(syn.text, syn.gen)
            }
        }
    }
    val meanText = tr.mean?.joinToString(", ") { it.text }
    Row {
        Spacer(modifier = Modifier.width(30.dp))
        if (meanText?.isNotBlank() == true) {
            Text(meanText, color = Color.LightGray, fontSize = 14.sp)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}
