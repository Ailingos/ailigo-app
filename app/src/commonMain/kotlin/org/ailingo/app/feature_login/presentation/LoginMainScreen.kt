package org.ailingo.app.feature_login.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import org.ailingo.app.core.util.VoiceToTextParser
import org.ailingo.app.feature_reset_password.presentation.ResetPasswordScreen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginMainScreen(
    navigator: Navigator,
    onLoginUser: () -> Unit,
    voiceToTextParser: VoiceToTextParser,
    login: String,
    onLoginChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibleChange: () -> Unit,
    isLoading: MutableState<Boolean>
) {
    val focusManager = LocalFocusManager.current
    val passwordFieldFocusRequester = rememberUpdatedState(FocusRequester())
    val keyboardController = LocalSoftwareKeyboardController.current
    val showLoginIsEmpty = remember {
        mutableStateOf(false)
    }
    val showPasswordIsEmpty = remember {
        mutableStateOf(false)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())
    ) {
        if (isLoading.value) {
            CircularProgressIndicator()
        } else {
            LoginWelcomeMessage()
            Spacer(modifier = Modifier.height(8.dp))
            LoginTextField(
                value = login,
                onValueChange = {
                    onLoginChange(it)
                },
                onNext = {
                    passwordFieldFocusRequester.value.requestFocus()
                },
                showErrorText = showLoginIsEmpty.value,
            )

            LoginPasswordTextField(
                value = password,
                onValueChange = {
                    onPasswordChange(it)
                },
                passwordVisible = passwordVisible,
                onPasswordVisibleChange = onPasswordVisibleChange,
                focusRequester = passwordFieldFocusRequester.value,
                focusManager = focusManager,
                keyboardController = keyboardController,
                onLoginUser = onLoginUser,
                showErrorText = showPasswordIsEmpty.value
            )
            Spacer(modifier = Modifier.height(12.dp))
            LoginForgotPasswordLink(onClick = { navigator.push(ResetPasswordScreen(voiceToTextParser)) })
            Spacer(modifier = Modifier.height(16.dp))
            LoginButton(
                onClick = onLoginUser,
                login,
                password,
                showLoginIsEmpty,
                showPasswordIsEmpty,
                isLoading
            )
            Spacer(modifier = Modifier.height(8.dp))
            LoginSignUpSection(navigator = navigator, voiceToTextParser = voiceToTextParser)
        }
    }
}