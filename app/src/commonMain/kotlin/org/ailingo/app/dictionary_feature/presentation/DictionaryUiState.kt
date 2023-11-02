package org.ailingo.app.dictionary_feature.presentation

import org.ailingo.app.dictionary_feature.data.DictionaryResponse

sealed class DictionaryUiState {
    object Loading : DictionaryUiState()
    object Empty : DictionaryUiState()
    data class Success(val response: DictionaryResponse) : DictionaryUiState()
    data class Error(val message: String) : DictionaryUiState()
}