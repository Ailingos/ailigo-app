package org.ailingo.app.feature_register.presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.ailingo.app.feature_register.data.model.SuccessRegister
import org.ailingo.app.feature_register.data.model.UserRegistrationData
import org.ailingo.app.feature_register.data.model_upload_image.UploadImageResponse
import org.ailingo.app.feature_register.data.model_upload_image.UploadImageUiState

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

    private val _imageState = MutableStateFlow<UploadImageUiState>(UploadImageUiState.EmptyImage)
    val imageState = _imageState.asStateFlow()

    private val baseUrlUploadImage = "https://api.imgbb.com/1/upload"
    private val uploadImageKey = "f90248ad8f4b1e262a5e8e7603645cc1"

    @OptIn(InternalAPI::class)
    fun uploadImage(base64Image: String) {
        _imageState.value = UploadImageUiState.LoadingImage
        val httpClient = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
            }
        }
        viewModelScope.launch {
            try {
                val response = httpClient.post("$baseUrlUploadImage?key=$uploadImageKey") {
                    body = MultiPartFormDataContent(
                        formData {
                            append("image", base64Image)
                        }
                    )
                }
                if (response.status.isSuccess()) {
                    val body = response.body<UploadImageResponse>()
                    _imageState.value = UploadImageUiState.Success(uploadImageResponse = body)
                } else {
                    _imageState.value = UploadImageUiState.Error("error: ${response.status}")
                }
            } catch (e: Exception) {
                _imageState.value = UploadImageUiState.Error("error: ${e.message}")
            } finally {
                httpClient.close()
            }
        }
    }

    fun backToEmptyUploadAvatar() {
        viewModelScope.launch {
            _imageState.value = UploadImageUiState.EmptyImage
        }
    }
}
