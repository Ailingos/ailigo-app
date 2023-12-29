package org.ailingo.app.core.helper_window_info

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.ailingo.app.getConfiguration
import org.ailingo.app.getPlatformName

@Composable
fun rememberWindowInfo(): WindowInfo {
    val density = LocalDensity.current.density

    val (width, height) = getConfiguration()
    return WindowInfo(
        screenWidthInfo = when {
            width < 840 / density || getPlatformName() == "Android" -> WindowInfo.WindowType.MobileWindowInfo
            else -> WindowInfo.WindowType.DesktopWindowInfo
        },
        screenHeightInfo = when {
            height < 900 / density || getPlatformName() == "Android" -> WindowInfo.WindowType.MobileWindowInfo
            else -> WindowInfo.WindowType.DesktopWindowInfo
        },
        screenWidth = (width / density).dp,
        screenHeight = (height / density).dp
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