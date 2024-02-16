package org.ailingo.app.feature_topics.presentation

import ailingo.app.generated.resources.Res
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import org.ailingo.app.TopicsForDesktopAndWeb
import org.ailingo.app.core.helper_window_info.WindowInfo
import org.ailingo.app.core.helper_window_info.rememberWindowInfo
import org.ailingo.app.feature_topics.data.Topic
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TopicsScreen(
    component: TopicsScreenComponent
) {
    val topics = listOf(
        Topic(Res.string.trips, Res.drawable.trips),
        Topic(Res.string.food_and_drinks, Res.drawable.food),
        Topic(Res.string.movies, Res.drawable.film),
        Topic(Res.string.cartoons, Res.drawable.cartoon),
        Topic(Res.string.culture_and_art, Res.drawable.culture),
        Topic(Res.string.technologies, Res.drawable.technology),
        Topic(Res.string.fashion_and_style, Res.drawable.fashion),
        Topic(Res.string.news, Res.drawable.news),
        Topic(Res.string.health_and_medicine, Res.drawable.health),
        Topic(Res.string.science_and_education, Res.drawable.science),
        Topic(Res.string.sport, Res.drawable.sport),
        Topic(Res.string.literature, Res.drawable.literature),
        Topic(Res.string.nature_and_ecology, Res.drawable.nature),
        Topic(Res.string.history, Res.drawable.history),
        Topic(Res.string.business, Res.drawable.business)
    )

    val screenInfo = rememberWindowInfo()
    if (screenInfo.screenWidthInfo is WindowInfo.WindowType.DesktopWindowInfo) {
        TopicsForDesktopAndWeb(topics)
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(topics) { topic ->
                TopicCardForMobile(topic)
            }
        }
    }
}