package org.ailingo.app.core.helper_window_info

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.ailingo.app.getPlatformName

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun rememberWindowInfo(): WindowInfo {
    val configuration = LocalWindowInfo.current.containerSize
    return WindowInfo(
        screenWidthInfo = when {
            configuration.width.dp < 840.dp || getPlatformName() == "Android" -> WindowInfo.WindowType.MobileWindowInfo
            else -> WindowInfo.WindowType.DesktopWindowInfo
        },
        screenHeightInfo = when {
            configuration.height.dp < 900.dp || getPlatformName() == "Android" -> WindowInfo.WindowType.MobileWindowInfo
            else -> WindowInfo.WindowType.DesktopWindowInfo
        },
        screenHeight = configuration.height.dp,
        screenWidth = configuration.width.dp
    )
}

data class WindowInfo(
    val screenWidthInfo: WindowType,
    val screenHeightInfo: WindowType,
    val screenWidth: Dp,
    val screenHeight: Dp
) {
    sealed class WindowType {
        object MobileWindowInfo : WindowType()
        object DesktopWindowInfo : WindowType()
    }
}