package com.example.feedback_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.TasksApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackHistoryViewModel @Inject constructor(
    private val tasksApi: TasksApi
) : ViewModel() {

    private val _uiState = MutableStateFlow<FeedbackHistoryUiState>(FeedbackHistoryUiState.Loading)
    val uiState: StateFlow<FeedbackHistoryUiState> = _uiState

    init {
        loadTexts()
    }

    private fun loadTexts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = tasksApi.getFeedbackHistory()
                _uiState.value = FeedbackHistoryUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = FeedbackHistoryUiState.Error(e.message.toString())
            }
        }
    }

    fun retry() {
        _uiState.value = FeedbackHistoryUiState.Loading
        loadTexts()
    }

}