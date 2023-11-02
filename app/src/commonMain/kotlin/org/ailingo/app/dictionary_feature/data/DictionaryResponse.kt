package org.ailingo.app.dictionary_feature.data

import kotlinx.serialization.Serializable

@Serializable
data class DictionaryResponse(
    val def: List<Def>? = emptyList(),
    val head: Head? = null
)