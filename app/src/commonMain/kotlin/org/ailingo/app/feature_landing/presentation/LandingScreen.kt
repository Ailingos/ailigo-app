package org.ailingo.app.feature_landing.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import compose.icons.FeatherIcons
import compose.icons.feathericons.Phone
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import org.ailingo.app.SharedRes
import org.ailingo.app.core.helper_window_info.rememberWindowInfo
import org.ailingo.app.core.util.VoiceToTextParser
import org.ailingo.app.feature_login.presentation.LoginScreen
import org.ailingo.app.theme.ColorForMainTextDictionary

data class LandingScreen(
    val voiceToTextParser: VoiceToTextParser
) : Screen {
    @Composable
    override fun Content() {
        val screenInfo = rememberWindowInfo()
        val screenHeight = screenInfo.screenHeight
        val screenWidth = screenInfo.screenWidth

        val infoItems = listOf(
            Info(
                FeatherIcons.Phone,
                "GitHub Pages Jekyll Theme",
                "Designed for GitHub Pages. Fork. Edit _config.yml. Upload screenshot/video. Push to gh-pages branch. Voilá!"
            ),
            Info(
                FeatherIcons.Phone,
                "iPhone Device Preview",
                "Preview your app in the context of an iPhone device. Five different device colors included."
            ),
            Info(
                FeatherIcons.Phone,
                "Video Support",
                "Preview app video on the iPhone device simply by placing your video files in the videos folder."
            ),
            Info(
                FeatherIcons.Phone,
                "Automatic Icon and Metadata",
                "Enter iOS app ID in the _config.yml file to automatically fetch app icon, price and App Store Link."
            ),
            Info(
                FeatherIcons.Phone,
                "Easy to Tweak",
                "Tweak accent color, images, icons and transparency via the _config.yml file. No HTML/CSS needed."
            ),
            Info(
                FeatherIcons.Phone,
                "Feature List",
                "Add features (like this one) to your site via the _config.yml file. No HTML/CSS needed."
            ),
            Info(
                FeatherIcons.Phone,
                "Smart App Banner",
                "Display a smart app banner on iOS devices."
            ),
            Info(
                FeatherIcons.Phone,
                "Social Links",
                "Easily add social media accounts and contact info in the footer via the _config.yml file. No HTML/CSS needed."
            ),
            Info(
                FeatherIcons.Phone,
                "FontAwesome Support",
                "Pick custom Font Awesome icons for the feature list via the _config.yml file. No HTML/CSS needed."
            ),
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                header {
                    Box(
                        modifier = Modifier.height(screenHeight).width(screenWidth)
                            .padding(bottom = 100.dp)
                    ) {
                        Image(
                            painter = painterResource(SharedRes.images.backHeader),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                                .padding(bottom = 100.dp)
                                .clip(
                                    RoundedCornerShape(
                                        bottomEndPercent = 5,
                                        bottomStartPercent = 5
                                    )
                                )
                        )
                        Column {
                            HeaderLanding(voiceToTextParser = voiceToTextParser)
                            Spacer(modifier = Modifier.height(64.dp))
                            DisplayPhoneAndGooglePlay(screenHeight, screenWidth)
                        }
                    }
                }
                itemsIndexed(infoItems) { index, infoAbout ->
                    InfoAbout(index, infoAbout)
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
                header {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Made by Emil Baehr in Copenhagen")
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
                header {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.Gray.copy(alpha = 0.2f), shape = CircleShape)
                                .aspectRatio(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = FeatherIcons.Phone,
                                contentDescription = null,
                                tint = Color.DarkGray
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.Gray.copy(alpha = 0.2f), shape = CircleShape)
                                .aspectRatio(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = FeatherIcons.Phone,
                                contentDescription = null,
                                tint = Color.DarkGray
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.Gray.copy(alpha = 0.2f), shape = CircleShape)
                                .aspectRatio(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = FeatherIcons.Phone,
                                contentDescription = null,
                                tint = Color.DarkGray
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
                header {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            32.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        Text(
                            "Whats new",
                            color = Color.Blue,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "Private Policy",
                            color = Color.Blue,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "Press kit",
                            color = Color.Blue,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }

}

@Composable
fun HeaderLanding(
    voiceToTextParser: VoiceToTextParser
) {
    val navigator = LocalNavigator.currentOrThrow
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(120.dp)
            .background(Color.LightGray.copy(alpha = 0.1f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize().padding(start = 64.dp, end = 64.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(SharedRes.images.ailingologowithoutbackground),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.height(40.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                onClick = {
                    navigator.push(LoginScreen(voiceToTextParser = voiceToTextParser))
                }, colors = ButtonDefaults.textButtonColors(
                    contentColor = ColorForMainTextDictionary
                )
            ) {
                Text(
                    stringResource(SharedRes.strings.login),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                )
            }
        }
    }
}


@Composable
fun DisplayPhoneAndGooglePlay(
    heightOfImage: Dp,
    widthOfImage: Dp
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 64.dp, end = 64.dp)
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                Image(
                    painterResource(SharedRes.images.LandgingPhone),
                    contentDescription = null,
                    modifier = Modifier
                )
            }
            Spacer(modifier = Modifier.width(32.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.weight(3f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(25),
                        colors = CardDefaults.cardColors(

                        ),
                        modifier = Modifier.size(100.dp)
                    ) {
                        Image(
                            painter = painterResource(SharedRes.images.logo),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().padding(16.dp)
                        )
                    }
                    Column {
                        Text(
                            "Google play Connect",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text("Free", color = Color.White)
                    }
                }
                Text(
                    "Write a short tagline for your app.",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(SharedRes.images.googlePlay),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Download on the")
                            Text("Google Play")
                        }
                    }
                }
            }

        }
    }
}


@Composable
fun InfoAbout(
    index: Int,
    infoAbout: Info
) {
    Row(
        modifier = Modifier.fillMaxSize()
            .padding(start = 64.dp, end = if (index % 3 == 2) 64.dp else 0.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.Gray.copy(alpha = 0.2f), shape = CircleShape).aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = infoAbout.icon, contentDescription = null, tint = Color.Blue)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                infoAbout.title,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(infoAbout.description, color = Color.DarkGray)
        }
    }
}

data class Info(
    val icon: ImageVector,
    val title: String,
    val description: String
)

fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}
