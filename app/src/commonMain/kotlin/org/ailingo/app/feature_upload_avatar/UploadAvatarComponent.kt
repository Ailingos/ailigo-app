package org.ailingo.app.feature_upload_avatar

import com.arkivanov.decompose.ComponentContext

class UploadAvatarComponent(
    componentContext: ComponentContext,
    val login: String,
    val password: String,
    val email: String,
    val name: String,
    private val onNavigateToChatScreen: () -> Unit,
    private val onNavigateToRegisterScreen: () -> Unit
): ComponentContext by componentContext {
    fun onEvent(event: UploadAvatarEvent) {
        when (event) {
            UploadAvatarEvent.OnNavigateToChatScreen -> {
                onNavigateToChatScreen()
            }
            UploadAvatarEvent.OnNavigateToRegisterScreen -> {
                onNavigateToRegisterScreen()
            }
        }
    }
}