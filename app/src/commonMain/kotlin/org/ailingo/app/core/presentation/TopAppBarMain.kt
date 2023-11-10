package org.ailingo.app.core.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Menu
import dev.icerock.moko.resources.compose.painterResource
import org.ailingo.app.SharedRes
import org.ailingo.app.core.helper_window_info.WindowInfo
import org.ailingo.app.core.helper_window_info.rememberWindowInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarMain(
    onOpenNavigation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenInfo = rememberWindowInfo()
    //val gradientColors = listOf(Color.Cyan, Color.Blue, Color.Magenta)
    val gradientColors = listOf(Color.Black, Color.Black)

    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        navigationIcon = {
            if (screenInfo.screenWidthInfo is WindowInfo.WindowType.MobileWindowInfo) {
                IconButton(
                    onClick = onOpenNavigation
                ) {
                    Icon(
                        imageVector = FeatherIcons.Menu, contentDescription = null
                    )
                }
            }
        },
        title = {
            Icon(
                painter = painterResource(SharedRes.images.ailingologowithoutbackground),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.graphicsLayer(alpha = 0.99f).height(30.dp)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(
                                Brush.linearGradient(gradientColors),
                                blendMode = BlendMode.SrcAtop
                            )
                        }
                    }
            )
        }
    )
}