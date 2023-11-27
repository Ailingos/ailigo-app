package org.ailingo.app

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.ailingo.app.database.HistoryDictionaryDatabase

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

actual fun selectImage(): String? {
    TODO("Not yet implemented")
}