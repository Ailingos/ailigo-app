package org.ailingo.app.feature_login.presentation

import com.arkivanov.decompose.ComponentContext

class LoginScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateToChatScreen: () -> Unit,
    private val onNavigateToResetPasswordScreen: () -> Unit,
    private val onNavigateToRegisterScreen: () -> Unit
) : ComponentContext by componentContext {
    fun onEvent(event: LoginScreenEvent) {
        when (event) {
            LoginScreenEvent.OnNavigateToChatScreen -> onNavigateToChatScreen()
            LoginScreenEvent.OnNavigateToResetPasswordScreen -> onNavigateToResetPasswordScreen()
            LoginScreenEvent.OnNavigateToRegisterScreen -> onNavigateToRegisterScreen()
        }
    }
}