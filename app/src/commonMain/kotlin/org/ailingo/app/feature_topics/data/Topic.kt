package org.ailingo.app.feature_topics.data

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource


data class Topic @OptIn(ExperimentalResourceApi::class) constructor(val title: StringResource, val image: DrawableResource)