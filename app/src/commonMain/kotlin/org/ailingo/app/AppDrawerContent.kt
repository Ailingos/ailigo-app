package org.ailingo.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.resources.compose.stringResource


@Composable
fun AppDrawerContent(voiceToTextParser: VoiceToTextParser) {
    val items = listOf(
        DrawerItems.FreeMode,
        DrawerItems.Topics,
        DrawerItems.Dictionary,
        DrawerItems.Exit,
    )
    val navigator = LocalNavigator.currentOrThrow
    LazyColumn {
        itemsIndexed(items) { index, item ->
            ListItem(
                modifier = Modifier.clickable {
                    when (item) {
                        DrawerItems.Dictionary -> {

                        }
                        DrawerItems.Exit ->  {
                            navigator.push(GetStartedScreen(voiceToTextParser))
                        }
                        DrawerItems.FreeMode -> {

                        }
                        DrawerItems.Topics -> {

                        }
                    }
                },
                leadingContent = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(
                            item.title
                        )
                    )
                },
                headlineContent = {
                    Text(text = stringResource(item.title))
                },
            )
        }
    }
}