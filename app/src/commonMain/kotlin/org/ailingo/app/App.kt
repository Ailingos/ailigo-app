package org.ailingo.app

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
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
                    login = mutableStateOf(TextFieldValue("")),
                    password = mutableStateOf(TextFieldValue("")),
                    email = mutableStateOf(TextFieldValue("")),
                    name = mutableStateOf(TextFieldValue("")),
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
                                if (navigator.lastItem.key != LandingScreen(voiceToTextParser).key) {
                                    TopAppBarForStart()
                                }
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

@Composable
expect fun CustomTextFieldImpl(
    textValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    supportingText: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
)