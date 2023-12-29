package org.ailingo.app.feature_topics.presentation

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.ailingo.app.SharedRes
import org.ailingo.app.core.helper_window_info.WindowInfo
import org.ailingo.app.core.helper_window_info.rememberWindowInfo
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
        val screenInfo = rememberWindowInfo()
        if (screenInfo.screenWidthInfo is WindowInfo.WindowType.DesktopWindowInfo) {
            val horizontalScrollbarStyle = ScrollbarStyle(
                minimalHeight = 0.dp,
                thickness = 12.dp,
                shape = RectangleShape,
                hoverDurationMillis = 100,
                unhoverColor = Color.Gray,
                hoverColor = Color.DarkGray
            )
            val horizontalScrollState = rememberScrollState(0)
            val topicsCount = topics.size
            val topicsPerColumn = 2
            val columnsCount = topicsCount / topicsPerColumn
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().horizontalScroll(horizontalScrollState).padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    repeat(columnsCount) { columnIndex ->
                        Column(modifier = Modifier.fillMaxHeight()) {
                            repeat(topicsPerColumn) { rowIndex ->
                                val topicIndex = columnIndex * topicsPerColumn + rowIndex
                                TopicCard(
                                    topics[topicIndex],
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
                HorizontalScrollbar(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth(),
                    adapter = rememberScrollbarAdapter(horizontalScrollState),
                    style = horizontalScrollbarStyle
                )
            }
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
}

