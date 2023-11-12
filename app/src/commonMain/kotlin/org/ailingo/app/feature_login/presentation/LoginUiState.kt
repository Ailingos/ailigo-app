package org.ailingo.app.feature_login.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class LoginUiState {
    @Serializable
    data class Success(
        val id: String? = "",
        val login: String? = "",
        val avatar: String? = "",
        val xp: Int? = 0,
        val registration: String? = "",
        val lastLoginAt: String? = ""
    ) : LoginUiState()

    data class Error(val message: String) : LoginUiState()
    object Loading : LoginUiState()
    object Empty : LoginUiState()
}