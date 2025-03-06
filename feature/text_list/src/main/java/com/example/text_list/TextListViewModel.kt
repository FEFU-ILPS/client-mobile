package com.example.text_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.TextApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextListViewModel @Inject constructor(
    private val textApi: TextApi
) : ViewModel() {

    private val _uiState = MutableStateFlow<TextListUiState>(TextListUiState.Loading)
    val uiState: StateFlow<TextListUiState> = _uiState

    init {
        loadTexts()
    }

    private fun loadTexts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = textApi.getTexts()
                _uiState.value = TextListUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = TextListUiState.Error(e.message.toString())
            }
        }
    }

    fun retry() {
        _uiState.value = TextListUiState.Loading
        loadTexts()
    }

}
