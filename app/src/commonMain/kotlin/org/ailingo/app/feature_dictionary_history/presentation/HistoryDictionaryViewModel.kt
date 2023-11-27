package org.ailingo.app.feature_dictionary_history.presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.ailingo.app.feature_dictionary_history.domain.DictionaryRepository
import org.ailingo.app.feature_dictionary_history.domain.HistoryDictionary

data class HistoryState(
    val history: List<HistoryDictionary> = emptyList()
)
class HistoryDictionaryViewModel(
    private val historyDictionaryRepository: Deferred<DictionaryRepository>
) : ViewModel() {
    private val _historyOfDictionaryState = MutableStateFlow(HistoryState())
    val historyOfDictionaryState: StateFlow<HistoryState> = _historyOfDictionaryState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = HistoryState()
    )

    init {
        viewModelScope.launch {
            try {
                val dictionaryRepository = historyDictionaryRepository.await()
                dictionaryRepository.getDictionaryHistory().collectLatest { history ->
                    _historyOfDictionaryState.value = _historyOfDictionaryState.value.copy(history = history)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveSearchedWord(word: HistoryDictionary) {
        viewModelScope.launch {
            try {
                val dictionaryRepository = historyDictionaryRepository.await()
                dictionaryRepository.insertWordToHistory(word)
            } catch (e: Exception) {
                // Handle exception, e.g., log or show an error message
                e.printStackTrace()
            }
        }
    }

    fun deleteFromHistory(id: Long) {
        viewModelScope.launch {
            try {
                val dictionaryRepository = historyDictionaryRepository.await()
                dictionaryRepository.deleteWordFromHistory(id)
            } catch (e: Exception) {
                // Handle exception, e.g., log or show an error message
                e.printStackTrace()
            }
        }
    }
}
