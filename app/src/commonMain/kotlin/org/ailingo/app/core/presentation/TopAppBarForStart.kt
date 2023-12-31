package org.ailingo.app.core.presentation

import androidx.compose.foundation.layout.height
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import org.ailingo.app.SharedRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarForStart() {
    CenterAlignedTopAppBar(
        title = {
            Icon(
                painter = painterResource(SharedRes.images.ailingologowithoutbackground),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.height(40.dp),
            )
        }
    )

}
