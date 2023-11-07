package org.ailingo.app.core.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.ailingo.app.core.presentation.utils.DrawerItems
import org.ailingo.app.core.util.VoiceToTextParser
import org.ailingo.app.feature_chat.presentation.ChatScreen
import org.ailingo.app.feature_dictionary.presentation.DictionaryScreen
import org.ailingo.app.feature_get_started.presentation.GetStartedScreen
import org.ailingo.app.feature_topics.presentation.TopicsScreen


@Composable
fun AppDrawerContent(
    voiceToTextParser: VoiceToTextParser,
    drawerState: DrawerState,
    scope: CoroutineScope,

    ) {
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
                            navigator.push(DictionaryScreen())
                            scope.launch {
                                drawerState.close()
                            }
                        }

                        DrawerItems.Exit -> {
                            navigator.push(GetStartedScreen(voiceToTextParser))
                            scope.launch {
                                drawerState.close()
                            }
                        }

                        DrawerItems.FreeMode -> {
                            scope.launch {
                                navigator.push(ChatScreen(voiceToTextParser))
                                drawerState.close()
                            }
                        }

                        DrawerItems.Topics -> {
                            scope.launch {
                                navigator.push(TopicsScreen())
                                drawerState.close()
                            }
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