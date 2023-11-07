package org.ailingo.app.core.presentation.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowRightCircle
import compose.icons.feathericons.Book
import compose.icons.feathericons.MessageCircle
import dev.icerock.moko.resources.StringResource
import org.ailingo.app.SharedRes

sealed class DrawerItems(val title: StringResource, val icon: ImageVector) {
    object FreeMode: DrawerItems(SharedRes.strings.free_mode, FeatherIcons.ArrowRightCircle)
    object Topics: DrawerItems(SharedRes.strings.topics, FeatherIcons.MessageCircle)
    object Dictionary: DrawerItems(SharedRes.strings.dictionary, FeatherIcons.Book)
    object Exit: DrawerItems(SharedRes.strings.exit, Icons.Filled.ExitToApp)
}