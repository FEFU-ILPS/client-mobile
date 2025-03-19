package com.example.feedback_history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.FeedbackApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackHistoryViewModel @Inject constructor(
    private val feedbackApi: FeedbackApi
) : ViewModel() {

    private val _uiState = MutableStateFlow<FeedbackHistoryUiState>(FeedbackHistoryUiState.Loading)
    val uiState: StateFlow<FeedbackHistoryUiState> = _uiState

    init {
        loadTexts()
    }

    private fun loadTexts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = feedbackApi.getFeedbackHistory()
                _uiState.value = FeedbackHistoryUiState.Success(response)
            } catch (e: Exception) {
                Log.e("wtf",e.message.toString())
                _uiState.value = FeedbackHistoryUiState.Error(e.message.toString())
            }
        }
    }

    fun retry() {
        _uiState.value = FeedbackHistoryUiState.Loading
        loadTexts()
    }

}