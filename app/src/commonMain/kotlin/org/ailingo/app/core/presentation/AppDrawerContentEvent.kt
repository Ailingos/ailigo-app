package org.ailingo.app.core.presentation

sealed class AppDrawerContentEvent {
    data object OnNavigateToDictionaryScreen : AppDrawerContentEvent()
    data object OnNavigateToGetStartedScreen : AppDrawerContentEvent()
    data object OnNavigateToChatScreen : AppDrawerContentEvent()
    data object OnNavigateToTopicsScreen : AppDrawerContentEvent()
}