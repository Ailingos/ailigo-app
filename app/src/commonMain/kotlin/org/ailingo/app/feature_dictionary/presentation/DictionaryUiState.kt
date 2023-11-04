package org.ailingo.app.feature_dictionary.presentation

import org.ailingo.app.feature_dictionary.data.model.DictionaryResponse

sealed class DictionaryUiState {
    object Loading : DictionaryUiState()
    object Empty : DictionaryUiState()
    data class Success(val response: DictionaryResponse) : DictionaryUiState()
    data class Error(val message: String) : DictionaryUiState()
}