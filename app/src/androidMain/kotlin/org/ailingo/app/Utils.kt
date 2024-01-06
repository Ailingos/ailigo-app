package org.ailingo.app

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import cafe.adriel.voyager.navigator.Navigator
import com.seiko.imageloader.rememberImagePainter
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import org.ailingo.app.core.util.VoiceToTextParser
import org.ailingo.app.database.HistoryDictionaryDatabase
import org.ailingo.app.feature_register.data.model.UserRegistrationData
import org.ailingo.app.feature_register.data.model_upload_image.UploadImageUiState
import org.ailingo.app.feature_register.presentation.RegisterScreen
import org.ailingo.app.feature_register.presentation.RegistrationViewModel
import org.ailingo.app.feature_topics.data.Topic

internal actual fun openUrl(url: String?) {
    val uri = url?.let { Uri.parse(it) } ?: return
    val intent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = uri
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    AndroidApp.INSTANCE.startActivity(intent)
}

internal actual fun getPlatformName(): String {
    return "Android"
}

actual fun playSound(sound: String) {
    val mediaPlayer = MediaPlayer()
    mediaPlayer.setDataSource(sound)
    mediaPlayer.prepare()
    mediaPlayer.start()
}


@Composable
internal actual fun getConfiguration(): Pair<Int, Int> {
    val configuration = LocalConfiguration.current
    return Pair(configuration.screenWidthDp, configuration.screenHeightDp)
}

actual class DriverFactory(private val context: Context) {
    actual suspend fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            HistoryDictionaryDatabase.Schema.synchronous(),
            context,
            "historyDictionary.db"
        )
    }
}

@Composable
actual fun UploadAvatarForPhone(
    navigator: Navigator,
    voiceToTextParser: VoiceToTextParser,
    registerViewModel: RegistrationViewModel,
    login: MutableState<TextFieldValue>,
    password: MutableState<TextFieldValue>,
    email: MutableState<TextFieldValue>,
    name: MutableState<TextFieldValue>,
    savedPhoto: MutableState<String>
) {
    val context = LocalContext.current
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var base64Image by remember {
        mutableStateOf<String?>(null)
    }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            base64Image = convertImageToBase64(context, uri)
        }
    )

    val imageState = registerViewModel.imageState.collectAsState()
    LaunchedEffect(base64Image) {
        if (base64Image?.isNotEmpty() == true) {
            registerViewModel.uploadImage(base64Image!!)
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(SharedRes.strings.lets_add_your_avatar),
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                stringResource(SharedRes.strings.lets_other_get_to_know_you),
                style = MaterialTheme.typography.titleMedium,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        Card(
                            shape = CircleShape,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            when (imageState.value) {
                                UploadImageUiState.EmptyImage -> {
                                    if (savedPhoto.value.isNotEmpty()) {
                                        Image(
                                            painter = rememberImagePainter(savedPhoto.value),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                                            contentScale = ContentScale.Crop,
                                        )
                                    } else {
                                        Image(
                                            painter = painterResource(SharedRes.images.defaultProfilePhoto),
                                            contentDescription = null,
                                            modifier = Modifier.aspectRatio(1f),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }

                                is UploadImageUiState.Error -> {
                                    SelectionContainer {
                                        Box(
                                            modifier = Modifier.fillMaxSize()
                                                .padding(start = 16.dp, end = 16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                (imageState.value as UploadImageUiState.Error).message,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }

                                UploadImageUiState.LoadingImage -> {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }

                                is UploadImageUiState.Success -> {
                                    savedPhoto.value = (imageState.value as UploadImageUiState.Success).uploadImageResponse.data.image.url
                                    Image(
                                        painter = rememberImagePainter(savedPhoto.value),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    ElevatedButton(onClick = {
                        navigator.push(RegisterScreen(voiceToTextParser = voiceToTextParser))
                    }, shape = MaterialTheme.shapes.small) {
                        Text("Back to the input fields")
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(bottom = ButtonDefaults.MinHeight + 16.dp)
                ) {

                    OutlinedButton(
                        onClick = {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            stringResource(SharedRes.strings.choose_image),
                            color = Color.Black
                        )
                    }

                    if (imageState.value is UploadImageUiState.Success && savedPhoto.value.isNotEmpty()) {
                        OutlinedButton(
                            onClick = {
                                savedPhoto.value = ""
                                base64Image = null
                                registerViewModel.backToEmptyUploadAvatar()
                            },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                "Delete avatar",
                                color = Color.Black
                            )
                        }
                    }

                    if (imageState.value is UploadImageUiState.EmptyImage && savedPhoto.value.isEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(SharedRes.images.ArrowForwardIOS),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                stringResource(SharedRes.strings.continue_with_default_image),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.DarkGray
                            )
                        }
                    }
                    if (imageState.value !is UploadImageUiState.LoadingImage) {
                        OutlinedButton(onClick = {
                            if (imageState.value is UploadImageUiState.Success && savedPhoto.value.isNotEmpty()) {
                                registerViewModel.registerUser(
                                    UserRegistrationData(
                                        login = login.value.text,
                                        password = password.value.text,
                                        email = email.value.text,
                                        name = name.value.text,
                                        avatar = (imageState.value as UploadImageUiState.Success).uploadImageResponse.data.url
                                    )
                                )
                            } else {
                                registerViewModel.registerUser(
                                    UserRegistrationData(
                                        login = login.value.text,
                                        password = password.value.text,
                                        email = email.value.text,
                                        name = name.value.text,
                                        avatar = ""
                                    )
                                )
                            }
                        }, shape = MaterialTheme.shapes.small) {
                            Text(stringResource(SharedRes.strings.continue_app))
                        }
                    }
                }
            }
        }
    }
}

private fun convertImageToBase64(context: Context, uri: Uri?): String {
    val inputStream = uri?.let { context.contentResolver.openInputStream(it) }
    val bytes = inputStream?.readBytes()
    inputStream?.close()

    return if (bytes != null) {
        Base64.encodeToString(bytes, Base64.DEFAULT)
    } else {
        ""
    }
}

@Composable
actual fun CustomTextFieldImpl(
    textValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    readOnly: Boolean,
    textStyle: TextStyle,
    label: @Composable (() -> Unit)?,
    placeholder: @Composable (() -> Unit)?,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    prefix: @Composable (() -> Unit)?,
    suffix: @Composable (() -> Unit)?,
    supportingText: @Composable (() -> Unit)?,
    isError: Boolean,
    visualTransformation: VisualTransformation,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    singleLine: Boolean,
    maxLines: Int,
    minLines: Int,
    interactionSource: MutableInteractionSource,
    shape: Shape,
    colors: TextFieldColors
) {
    OutlinedTextField(
        value = textValue,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        textStyle = textStyle,
        isError = false,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        visualTransformation = visualTransformation,
        minLines = minLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors
    )
}


@Composable
actual fun TopicsForDesktopAndWeb(topics: List<Topic>) {}

actual suspend fun selectImageWebAndDesktop(): String? { return null }