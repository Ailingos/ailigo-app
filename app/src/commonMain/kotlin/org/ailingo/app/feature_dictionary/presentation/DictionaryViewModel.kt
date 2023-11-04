package org.ailingo.app.feature_dictionary.presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.ailingo.app.feature_dictionary.data.model.DictionaryResponse

class DictionaryViewModel : ViewModel() {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private val apiKey = "dict.1.1.20231102T140345Z.9979700cf66f91d0.b210308b827953080f07e8f2e12779e2486d2695"
    private val baseUrl = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup"

    private val _uiState = MutableStateFlow<DictionaryUiState>(DictionaryUiState.Empty)
    val uiState: StateFlow<DictionaryUiState> = _uiState.asStateFlow()

    fun searchWordDefinition(word: String) {
        viewModelScope.launch {
            try {
                _uiState.value = DictionaryUiState.Loading
                val response = httpClient
                    .get("$baseUrl?key=$apiKey&lang=en-ru&text=$word")
                    .body<DictionaryResponse>()
                _uiState.value = DictionaryUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value =
                    DictionaryUiState.Error("Error occurred while fetching data. ${e.message.toString()}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        httpClient.close()
    }
}
