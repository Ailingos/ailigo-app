package org.ailingo.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import app.cash.sqldelight.db.SqlDriver
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import org.ailingo.app.core.helper_window_info.WindowInfo
import org.ailingo.app.core.helper_window_info.rememberWindowInfo
import org.ailingo.app.core.presentation.AppDrawerContent
import org.ailingo.app.core.presentation.TopAppBarForStart
import org.ailingo.app.core.presentation.TopAppBarMain
import org.ailingo.app.core.util.VoiceToTextParser
import org.ailingo.app.feature_dictionary_history.domain.DictionaryRepository
import org.ailingo.app.feature_get_started.presentation.GetStartedScreen
import org.ailingo.app.feature_landing.presentation.LandingScreen
import org.ailingo.app.feature_login.presentation.LoginScreen
import org.ailingo.app.feature_register.presentation.RegisterScreen
import org.ailingo.app.feature_register.presentation.RegisterUploadAvatarScreen
import org.ailingo.app.feature_reset_password.presentation.ResetPasswordScreen
import org.ailingo.app.theme.AppTheme

@Composable
internal fun App(
    voiceToTextParser: VoiceToTextParser,
    historyDictionaryRepository: Deferred<DictionaryRepository>
) {
    AppTheme {
        Navigator(LandingScreen(voiceToTextParser)) { navigator ->
            val authScreens = listOf(
                LoginScreen(voiceToTextParser).key,
                RegisterScreen(voiceToTextParser).key,
                GetStartedScreen(voiceToTextParser).key,
                ResetPasswordScreen(voiceToTextParser).key,
                LandingScreen(voiceToTextParser).key,
                RegisterUploadAvatarScreen(
                    login = mutableStateOf(""),
                    password = mutableStateOf(""),
                    email = mutableStateOf(""),
                    name = mutableStateOf(""),
                    voiceToTextParser = voiceToTextParser,
                    savedPhoto = mutableStateOf("")
                ).key
            )
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val windowInfo = rememberWindowInfo()
            if (windowInfo.screenWidthInfo is WindowInfo.WindowType.DesktopWindowInfo) {
                Column {
                    if (navigator.lastItem.key !in authScreens) {
                        TopAppBarMain(
                            onOpenNavigation = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        )
                    } else {
                        if (navigator.lastItem.key != LandingScreen(voiceToTextParser).key) {
                            TopAppBarForStart()
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (navigator.lastItem.key !in authScreens) {
                            AppDrawerContent(
                                historyDictionaryRepository,
                                voiceToTextParser,
                                drawerState,
                                scope
                            )
                        }
                        CurrentScreen()
                    }
                }
            } else {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        if (navigator.lastItem.key !in authScreens) {
                            ModalDrawerSheet {
                                AppDrawerContent(
                                    historyDictionaryRepository,
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
                        Box(modifier = Modifier.padding(padding)) {
                            CurrentScreen()
                        }
                    }
                }
            }
        }
    }
}


internal expect fun openUrl(url: String?)
internal expect fun getPlatformName(): String
internal expect fun playSound(sound: String)

@Composable
internal expect fun getConfiguration(): Pair<Int, Int>

expect class DriverFactory {
    suspend fun createDriver(): SqlDriver
}

expect suspend fun selectImage(): String?



