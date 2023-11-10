package org.ailingo.app.feature_topics.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.ailingo.app.SharedRes
import org.ailingo.app.feature_topics.data.Topic

class TopicsScreen : Screen {
    @Composable
    override fun Content() {
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
        LazyHorizontalStaggeredGrid(
            rows = StaggeredGridCells.Adaptive(200.dp),
            horizontalItemSpacing = 8.dp,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp),
            content = {
                items(topics) { topic ->
                    TopicCard(topic)
                }
            }
        )
    }
}

