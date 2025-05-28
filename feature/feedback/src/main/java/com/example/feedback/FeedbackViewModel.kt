package com.example.feedback

import android.util.Log
import androidx.lifecycle.SavedStateHandle
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
class FeedbackViewModel @Inject constructor(
    private val tasksApi: TasksApi,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<FeedbackUiState>(FeedbackUiState.Loading)
    val uiState: StateFlow<FeedbackUiState> = _uiState
    private val feedbackId = savedStateHandle.get<String>("feedbackId")

    init {
        loadFeedback()
    }

    fun loadFeedback() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.value = FeedbackUiState.Success(tasksApi.getTaskInfo(feedbackId!!))
            } catch (e: Exception) {
                Log.e("err", e.message.toString())
                _uiState.value = FeedbackUiState.Error(e.message.toString())
            }
        }
    }


}