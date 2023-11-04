package org.ailingo.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.launch
import org.ailingo.app.core.presentation.AppDrawerContent
import org.ailingo.app.core.presentation.TopAppBarForStart
import org.ailingo.app.core.presentation.TopAppBarMain
import org.ailingo.app.core.util.VoiceToTextParser
import org.ailingo.app.feature_get_started.presentation.GetStartedScreen
import org.ailingo.app.feature_login.presentation.LoginScreen
import org.ailingo.app.feature_register.presentation.RegistrationScreen
import org.ailingo.app.feature_reset_password.presentation.ResetPasswordScreen
import org.ailingo.app.theme.AppTheme

@Composable
internal fun App(voiceToTextParser: VoiceToTextParser) {
    AppTheme {
        Navigator(GetStartedScreen(voiceToTextParser)) { navigator ->
            val authScreens = listOf(
                LoginScreen(voiceToTextParser).key,
                RegistrationScreen(voiceToTextParser).key,
                GetStartedScreen(voiceToTextParser).key,
                ResetPasswordScreen(voiceToTextParser).key
            )

            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    if (navigator.lastItem.key !in authScreens) {
                        ModalDrawerSheet {
                            AppDrawerContent(
                                voiceToTextParser,
                                drawerState,
                                scope
                            )
                        }
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        if (navigator.lastItem.key !in authScreens) {
                            TopAppBarMain(
                                onOpenNavigation = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            )
                        } else {
                            TopAppBarForStart()
                        }
                    },
                ) { padding ->
                    if (navigator.lastItem.key !in authScreens) {
                        Box(modifier = Modifier.padding(padding)) {
                            CurrentScreen()
                        }
                    } else {
                        CurrentScreen()
                    }

                }
            }
        }
    }
}


internal expect fun openUrl(url: String?)
