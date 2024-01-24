package org.ailingo.app.feature_topics.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import org.ailingo.app.SharedRes
import org.ailingo.app.TopicsForDesktopAndWeb
import org.ailingo.app.core.helper_window_info.WindowInfo
import org.ailingo.app.core.helper_window_info.rememberWindowInfo
import org.ailingo.app.feature_topics.data.Topic

@Composable
fun TopicsScreen(
    component: TopicsScreenComponent
) {
    val topics = listOf(
        Topic(SharedRes.strings.trips, SharedRes.images.trips),
        Topic(SharedRes.strings.food_and_drinks, SharedRes.images.food),
        Topic(SharedRes.strings.movies, SharedRes.images.film),
        Topic(SharedRes.strings.cartoons, SharedRes.images.cartoon),
        Topic(SharedRes.strings.culture_and_art, SharedRes.images.culture),
        Topic(SharedRes.strings.technologies, SharedRes.images.technology),
        Topic(SharedRes.strings.fashion_and_style, SharedRes.images.fashion),
        Topic(SharedRes.strings.news, SharedRes.images.news),
        Topic(SharedRes.strings.health_and_medicine, SharedRes.images.health),
        Topic(SharedRes.strings.science_and_education, SharedRes.images.science),
        Topic(SharedRes.strings.sport, SharedRes.images.sport),
        Topic(SharedRes.strings.literature, SharedRes.images.literature),
        Topic(SharedRes.strings.nature_and_ecology, SharedRes.images.nature),
        Topic(SharedRes.strings.history, SharedRes.images.history),
        Topic(SharedRes.strings.business, SharedRes.images.business)
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