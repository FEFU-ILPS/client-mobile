package com.example.feedback

import com.example.api.dto.FeedbackResponse

sealed interface FeedbackUiState {

    object Loading : FeedbackUiState

    data class Error(
        val message: String
    ) : FeedbackUiState

    data class Success(
        val response: FeedbackResponse
    ) : FeedbackUiState
}