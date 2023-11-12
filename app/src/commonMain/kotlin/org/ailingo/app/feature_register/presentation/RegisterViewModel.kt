package org.ailingo.app.feature_register.presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ailingo.app.feature_register.data.model.SuccessRegister
import org.ailingo.app.feature_register.data.model.UserRegistrationData

class RegistrationViewModel : ViewModel() {
    private val _registerState = MutableStateFlow<RegisterUiState>(RegisterUiState.Empty)
    val registerState: StateFlow<RegisterUiState> = _registerState.asStateFlow()

    private val BASE_URL = "https://app.artux.net/ailingo"
    private val API_ENDPOINT = "/api/v1/user/register"

    fun registerUser(user: UserRegistrationData) {
        viewModelScope.launch {
            _registerState.value = RegisterUiState.Loading
            val httpClient = HttpClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            try {
                val response = httpClient.post("$BASE_URL$API_ENDPOINT") {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    setBody(user)
                }
                _registerState.value = when {
                    response.status.isSuccess() -> {
                        val body = response.body<SuccessRegister>()
                        if (body.success) {
                            RegisterUiState.Success(
                                body.success,
                                body.code,
                                body.description,
                                body.failure
                            )
                        } else RegisterUiState.Error(body.description)
                    }
                    else -> RegisterUiState.Error("Request failed with $response")
                }
            } catch (e: Exception) {
                _registerState.update {
                    RegisterUiState.Error(e.message.toString())
                }
            } finally {
                httpClient.close()
            }
        }
    }

    fun backToEmptyState() {
        _registerState.value = RegisterUiState.Empty
    }
}
