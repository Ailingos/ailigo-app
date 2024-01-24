package org.ailingo.app.feature_upload_avatar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import org.ailingo.app.feature_register.presentation.RegisterError
import org.ailingo.app.feature_register.presentation.RegisterLoading
import org.ailingo.app.feature_register.presentation.RegisterUiState
import org.ailingo.app.feature_register.presentation.RegistrationViewModel

@Composable
fun UploadAvatarScreen(
    component: UploadAvatarComponent,
    login: String,
    password: String,
    email: String,
    name: String,
    onNavigateToRegisterScreen: () -> Unit
) {
    val registerViewModel = getViewModel(Unit, viewModelFactory { RegistrationViewModel() })
    val registerState = registerViewModel.registerState.collectAsState()
    when (registerState.value) {
        RegisterUiState.Empty -> {
            RegisterUploadAvatarEmpty(
                registerViewModel,
                login,
                password,
                email,
                name,
                onNavigateToRegisterScreen
            )
        }

        is RegisterUiState.Error -> {
            RegisterError(
                errorMessage = (registerState.value as RegisterUiState.Error).message,
                onBackToEmptyState = {
                    registerViewModel.backToEmptyState()
                    component.onEvent(
                        UploadAvatarEvent.OnNavigateToRegisterScreen
                    )
                })
        }

        RegisterUiState.Loading -> {
            RegisterLoading()
        }

        is RegisterUiState.Success -> {
            component.onEvent(
                UploadAvatarEvent.OnNavigateToChatScreen
            )
        }
    }
}