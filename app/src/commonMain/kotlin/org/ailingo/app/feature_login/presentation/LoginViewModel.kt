package org.ailingo.app.feature_login.presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ailingo.app.core.helper_auth.auth.basicAuthHeader

class LoginViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Empty)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    private val BASE_URL = "https://app.artux.net/ailingo"
    private val API_ENDPOINT = "/api/v1/user/info"

    fun loginUser(
        login: String,
        password: String
    ) {
        viewModelScope.launch {
            val httpClient = HttpClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            _loginState.value = LoginUiState.Loading
            try {
                val response = httpClient.get("$BASE_URL$API_ENDPOINT") {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    header(HttpHeaders.Authorization, basicAuthHeader(login, password))
                }
                _loginState.value = when {
                    response.status.isSuccess() -> {
                        val body = response.body<LoginUiState.Success>()
                        LoginUiState.Success(
                            body.id,
                            body.login,
                            body.avatar,
                            body.xp,
                            body.registration,
                            body.lastLoginAt
                        )
                    } else -> {
                        if (response.status.value == 401) {
                            LoginUiState.Error("Wrong login or password")
                        } else {
                            LoginUiState.Error("Request failed with ${response.status.value}")
                        }
                    }
                }
            } catch (e: Exception) {
                _loginState.update {
                    LoginUiState.Error(e.message.toString())
                }
            } finally {
                httpClient.close()
            }
        }
    }

    fun backToEmptyLoginState() {
        _loginState.value = LoginUiState.Empty
    }

    override fun onCleared() {
        super.onCleared()
    }
}