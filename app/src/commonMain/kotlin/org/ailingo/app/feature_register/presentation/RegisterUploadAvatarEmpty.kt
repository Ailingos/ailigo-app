package org.ailingo.app.feature_register.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.seiko.imageloader.rememberImagePainter
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch
import org.ailingo.app.SharedRes
import org.ailingo.app.core.util.VoiceToTextParser
import org.ailingo.app.feature_register.data.model.UserRegistrationData
import org.ailingo.app.feature_register.data.model_upload_image.UploadImageUiState
import org.ailingo.app.selectImage


@Composable
fun RegisterUploadAvatarEmpty(
    navigator: Navigator,
    voiceToTextParser: VoiceToTextParser,
    registerViewModel: RegistrationViewModel,
    login: MutableState<TextFieldValue>,
    password: MutableState<TextFieldValue>,
    email: MutableState<TextFieldValue>,
    name: MutableState<TextFieldValue>,
    savedPhoto: MutableState<String>
) {
    val imageState = registerViewModel.imageState.collectAsState()
    var base64Image by remember {
        mutableStateOf<String?>(null)
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(base64Image) {
        if (base64Image?.isNotEmpty() == true) {
            print(base64Image)
            registerViewModel.uploadImage(base64Image!!)
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                Text(
                    stringResource(SharedRes.strings.lets_add_your_avatar),
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    stringResource(SharedRes.strings.lets_other_get_to_know_you),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.DarkGray,
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
                            modifier = Modifier.size(300.dp)
                        ) {
                            Card(
                                shape = CircleShape,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                when (imageState.value) {
                                    UploadImageUiState.EmptyImage -> {
                                        if (savedPhoto.value.isNotEmpty()) {
                                            Image(
                                                painter = rememberImagePainter(savedPhoto.value),
                                                contentDescription = null,
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        } else {
                                            Image(
                                                painter = painterResource(SharedRes.images.defaultProfilePhoto),
                                                contentDescription = null,
                                                modifier = Modifier.fillMaxSize(),
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
                                            modifier = Modifier.fillMaxSize(),
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
                                            modifier = Modifier.fillMaxSize(),
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
                                scope.launch {
                                    base64Image = selectImage()
                                }
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
}
