package org.ailingo.app.feature_upload_avatar

sealed class UploadAvatarEvent {
    data object OnNavigateToRegisterScreen: UploadAvatarEvent()
    data object OnNavigateToChatScreen: UploadAvatarEvent()
}