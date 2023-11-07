package org.ailingo.app.feature_dictionary.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import org.ailingo.app.feature_dictionary.presentation.utils.ErrorState
import org.ailingo.app.feature_dictionary.presentation.utils.LoadingState

class DictionaryScreen : Screen {
    @Composable
    override fun Content() {
        val dictionaryViewModel = getViewModel(Unit, viewModelFactory { DictionaryViewModel() })
        val uiState = dictionaryViewModel.uiState.collectAsState()

        var textFieldValue by rememberSaveable { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp)
        ) {
            SearchTextField(textFieldValue, onTextFieldValueChange = { textFieldValue = it }) {
                dictionaryViewModel.searchWordDefinition(textFieldValue)
            }

            when (val currentState = uiState.value) {
                is DictionaryUiState.Loading -> {
                    LoadingState()
                }

                is DictionaryUiState.Success -> {
                    val response = currentState.response
                    if (response.def?.isNotEmpty() == true) {
                        DefinitionList(definitions = response.def)
                    } else {
                        Text("No definitions found for the word.")
                    }
                }

                is DictionaryUiState.Error -> {
                    val errorMessage = currentState.message
                    ErrorState(errorMessage)
                }

                DictionaryUiState.Empty -> {
                    // Handle empty state if needed
                }
            }
        }
    }
}
