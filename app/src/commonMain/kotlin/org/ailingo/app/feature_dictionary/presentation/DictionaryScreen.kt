package org.ailingo.app.feature_dictionary.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.Deferred
import org.ailingo.app.SharedRes
import org.ailingo.app.feature_dictionary.presentation.utils.ErrorDictionaryScreen
import org.ailingo.app.feature_dictionary.presentation.utils.LoadingDictionaryScreen
import org.ailingo.app.feature_dictionary_history.domain.DictionaryRepository
import org.ailingo.app.feature_dictionary_history.presentation.HistoryDictionaryViewModel


data class DictionaryScreen(
    val historyDictionaryRepository: Deferred<DictionaryRepository>
) : Screen {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val dictionaryViewModel = getViewModel(Unit, viewModelFactory { DictionaryViewModel() })
        val uiState = dictionaryViewModel.uiState.collectAsState()

        val historyDictionaryViewModel = getViewModel(Unit, factory = viewModelFactory {
            HistoryDictionaryViewModel(historyDictionaryRepository = historyDictionaryRepository)
        })
        val historyState = historyDictionaryViewModel.historyOfDictionaryState.collectAsState()
        val textFieldValue = rememberSaveable { mutableStateOf("") }
        val active = remember {
            mutableStateOf(false)
        }
        var searchBarHeight = remember { mutableStateOf(0) }
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                stickyHeader {
                    SearchTextFieldDictionary(
                        dictionaryViewModel = dictionaryViewModel,
                        textFieldValue = textFieldValue,
                        onTextFieldValueChange = { newTextFieldValue ->
                            textFieldValue.value = newTextFieldValue
                        },
                        historyDictionaryViewModel = historyDictionaryViewModel,
                        active = active,
                        searchBarHeight
                    ) { searchWord ->
                        dictionaryViewModel.searchWordDefinition(searchWord)
                    }
                }
                if (uiState.value is DictionaryUiState.Empty) {
                    items(historyState.value.history) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(14.dp).clickable {
                                textFieldValue.value = it.text
                                dictionaryViewModel.searchWordDefinition(it.text)
                                active.value = false
                            }) {
                            Icon(
                                modifier = Modifier.padding(end = 10.dp),
                                imageVector = Icons.Default.History,
                                contentDescription = null
                            )
                            Text(text = it.text)
                        }
                    }
                }

                if (uiState.value is DictionaryUiState.Success) {
                    val response = (uiState.value as DictionaryUiState.Success).response
                    val responseForExamples = (uiState.value as DictionaryUiState.Success).responseExample
                    val listOfExamples = responseForExamples?.flatMap {
                        it.meanings.flatMap { meaning ->
                            meaning.definitions.mapNotNull { def ->
                                def.example
                            }
                        }
                    }
                    val listOfDefinitions = responseForExamples?.flatMap {
                        it.meanings.flatMap { meaning ->
                            meaning.definitions.map { def ->
                                def.definition
                            }
                        }
                    }
                    if (response.def?.isNotEmpty() == true) {
                        items(response.def) { definition ->
                            DefinitionRowInfo(definition, responseForExamples)
                        }
                        item {
                            if (listOfExamples?.isNotEmpty() == true) {
                                Text(
                                    stringResource(SharedRes.strings.usage_examples),
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                        item {
                            ListOfExample(listOfExamples, textFieldValue.value)
                        }
                        item {
                            if (listOfDefinitions?.isNotEmpty() == true) {
                                Text(
                                    stringResource(SharedRes.strings.definitions),
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                        item {
                            ListOfDefinitions(listOfDefinitions)
                        }
                    } else {
                        item {
                            Text(stringResource(SharedRes.strings.no_definitions))
                        }
                    }
                }
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                when (uiState.value) {
                    DictionaryUiState.Empty -> {
                        if (historyState.value.history.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize()
                                    .padding(top = searchBarHeight.value.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Here will be the search history of your words")
                            }
                        }
                    }

                    is DictionaryUiState.Error -> {
                        ErrorDictionaryScreen(
                            (uiState.value as DictionaryUiState.Error).message,
                            modifier = Modifier.padding(top = searchBarHeight.value.dp)
                        )
                    }

                    DictionaryUiState.Loading -> {
                        LoadingDictionaryScreen(modifier = Modifier.padding(top = searchBarHeight.value.dp))
                    }

                    else -> {}
                }
            }
        }
    }
}
