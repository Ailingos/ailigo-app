package org.ailingo.app

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.compose.stringResource


@Composable
fun AppDrawerContent() {
    val items = listOf(
        DrawerItems.FreeMode,
        DrawerItems.Topics,
        DrawerItems.Dictionary,
        DrawerItems.Exit,
    )
    LazyColumn {
        itemsIndexed(items) { index, item ->
            ListItem(
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