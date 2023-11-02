package org.ailingo.app.dictionary_feature.data

import kotlinx.serialization.Serializable

@Serializable
data class Def(
    val pos: String,
    val text: String,
    val tr: List<Tr>,
    val ts: String
)