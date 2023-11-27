package org.ailingo.app.feature_dictionary_history.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.ailingo.app.DriverFactory
import org.ailingo.app.database.HistoryDictionaryDatabase
import org.ailingo.app.feature_dictionary_history.data.DictionaryRepositoryImpl
import org.ailingo.app.feature_dictionary_history.domain.DictionaryRepository

actual class AppModule {
    actual val dictionaryRepository: Deferred<DictionaryRepository> = CoroutineScope(Dispatchers.Default).async {
        DictionaryRepositoryImpl(
            db = HistoryDictionaryDatabase(
                driver = DriverFactory().createDriver()
            )
        )
    }
}