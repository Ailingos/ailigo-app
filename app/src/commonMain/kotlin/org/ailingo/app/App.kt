package org.ailingo.app

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import app.cash.sqldelight.db.SqlDriver
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import org.ailingo.app.core.helper_window_info.WindowInfo
import org.ailingo.app.core.helper_window_info.rememberWindowInfo
import org.ailingo.app.core.presentation.AppDrawerContent
import org.ailingo.app.core.presentation.TopAppBarForStart
import org.ailingo.app.core.presentation.TopAppBarMain
import org.ailingo.app.core.util.VoiceToTextParser
import org.ailingo.app.feature_chat.presentation.ChatScreen
import org.ailingo.app.feature_dictionary.presentation.DictionaryScreen
import org.ailingo.app.feature_dictionary_history.domain.DictionaryRepository
import org.ailingo.app.feature_get_started.presentation.GetStartedScreen
import org.ailingo.app.feature_landing.presentation.LandingScreen
import org.ailingo.app.feature_login.presentation.LoginScreen
import org.ailingo.app.feature_register.presentation.RegisterScreen
import org.ailingo.app.feature_register.presentation.RegistrationViewModel
import org.ailingo.app.feature_reset_password.presentation.ResetPasswordScreen
import org.ailingo.app.feature_topics.data.Topic
import org.ailingo.app.feature_topics.presentation.TopicsScreen
import org.ailingo.app.feature_upload_avatar.UploadAvatarScreen
import org.ailingo.app.theme.AppTheme

@Composable
internal fun App(
    voiceToTextParser: VoiceToTextParser,
    historyDictionaryRepository: Deferred<DictionaryRepository>,
    root: RootComponent
) {
    var currentScreen by remember { mutableStateOf<RootComponent.Child?>(null) }
    val childStack by root.childStack.subscribeAsState()
    val windowInfo = rememberWindowInfo()
    AppTheme {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        if (windowInfo.screenWidthInfo is WindowInfo.WindowType.DesktopWindowInfo) {
            Scaffold(
                topBar = {
                    when (currentScreen) {
                        is RootComponent.Child.GetStartedScreen,
                        is RootComponent.Child.LoginScreen,
                        is RootComponent.Child.RegisterScreen,
                        is RootComponent.Child.ResetPasswordScreen,
                        is RootComponent.Child.UploadAvatarScreen -> {
                            TopAppBarForStart()
                        }

                        is RootComponent.Child.LandingScreen -> {}

                        else -> TopAppBarMain(
                            onOpenNavigation = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isOpen) close() else open()
                                    }
                                }
                            }
                        )
                    }
                }
            ) { paddingValues ->
                Children(
                    stack = childStack,
                    animation = stackAnimation(fade()),
                    modifier = Modifier.padding(paddingValues)
                ) { child ->
                    currentScreen = child.instance
                    Row {
                        when (currentScreen) {
                            is RootComponent.Child.GetStartedScreen,
                            is RootComponent.Child.LoginScreen,
                            is RootComponent.Child.RegisterScreen,
                            is RootComponent.Child.ResetPasswordScreen,
                            is RootComponent.Child.UploadAvatarScreen,
                            is RootComponent.Child.LandingScreen -> {}

                            else -> {
                                AppDrawerContent(
                                    drawerState = drawerState,
                                    root = root
                                )
                            }
                        }
                        when (val instance = child.instance) {
                            is RootComponent.Child.LandingScreen -> LandingScreen(instance.component)
                            is RootComponent.Child.LoginScreen -> LoginScreen(instance.component)
                            is RootComponent.Child.ChatScreen -> ChatScreen(
                                voiceToTextParser,
                                instance.component
                            )

                            is RootComponent.Child.GetStartedScreen -> GetStartedScreen(instance.component)
                            is RootComponent.Child.ResetPasswordScreen -> ResetPasswordScreen(
                                instance.component
                            )

                            is RootComponent.Child.RegisterScreen -> RegisterScreen(instance.component)
                            is RootComponent.Child.UploadAvatarScreen -> UploadAvatarScreen(
                                instance.component,
                                instance.component.login,
                                instance.component.password,
                                instance.component.email,
                                instance.component.name,
                                onNavigateToRegisterScreen = {
                                    root.navigateToRegisterScreen()
                                }
                            )

                            RootComponent.Child.TopicsScreen -> {
                                TopicsScreen()
                            }

                            RootComponent.Child.DictionaryScreen -> {
                                DictionaryScreen(historyDictionaryRepository)
                            }
                        }
                    }
                }
            }
        } else {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    when (currentScreen) {
                        is RootComponent.Child.LandingScreen,
                        is RootComponent.Child.GetStartedScreen,
                        is RootComponent.Child.LoginScreen,
                        is RootComponent.Child.RegisterScreen,
                        is RootComponent.Child.ResetPasswordScreen,
                        is RootComponent.Child.UploadAvatarScreen -> {}

                        else -> {
                            ModalDrawerSheet {
                                AppDrawerContent(
                                    drawerState,
                                    root
                                )
                            }
                        }
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        when (currentScreen) {
                            is RootComponent.Child.GetStartedScreen,
                            is RootComponent.Child.LoginScreen,
                            is RootComponent.Child.RegisterScreen,
                            is RootComponent.Child.ResetPasswordScreen,
                            is RootComponent.Child.UploadAvatarScreen -> {
                                TopAppBarForStart()
                            }

                            is RootComponent.Child.LandingScreen -> {}

                            else -> TopAppBarMain(
                                onOpenNavigation = {
                                    scope.launch {
                                        drawerState.apply {
                                            if (isOpen) close() else open()
                                        }
                                    }
                                }
                            )
                        }
                    }
                ) { paddingValues ->
                    Children(
                        stack = childStack,
                        animation = stackAnimation(fade()),
                        modifier = Modifier.padding(paddingValues)
                    ) { child ->
                        currentScreen = child.instance
                        when (val instance = child.instance) {
                            is RootComponent.Child.LandingScreen -> LandingScreen(instance.component)
                            is RootComponent.Child.LoginScreen -> LoginScreen(instance.component)
                            is RootComponent.Child.ChatScreen -> ChatScreen(
                                voiceToTextParser,
                                instance.component
                            )

                            is RootComponent.Child.GetStartedScreen -> GetStartedScreen(instance.component)
                            is RootComponent.Child.ResetPasswordScreen -> ResetPasswordScreen(
                                instance.component
                            )

                            is RootComponent.Child.RegisterScreen -> RegisterScreen(instance.component)
                            is RootComponent.Child.UploadAvatarScreen -> UploadAvatarScreen(
                                instance.component,
                                instance.component.login,
                                instance.component.password,
                                instance.component.email,
                                instance.component.name,
                                onNavigateToRegisterScreen = {
                                    root.navigateToRegisterScreen()
                                }
                            )

                            RootComponent.Child.TopicsScreen -> {
                                TopicsScreen()
                            }

                            RootComponent.Child.DictionaryScreen -> {
                                DictionaryScreen(historyDictionaryRepository)
                            }
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

expect suspend fun selectImageWebAndDesktop(): String?

@Composable
expect fun UploadAvatarForPhone(
    registerViewModel: RegistrationViewModel,
    login: String,
    password: String,
    email: String,
    name: String,
    onNavigateToRegisterScreen: () -> Unit
)

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


@Composable
expect fun TopicsForDesktopAndWeb(topics: List<Topic>)