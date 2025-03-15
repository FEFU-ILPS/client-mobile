package com.example.text

sealed interface FeedbackUiState {

    object Loading : FeedbackUiState

    object NotSet : FeedbackUiState

    data class Error(
        val message: String
    ) : FeedbackUiState

    data class Success(
        val feedbackId: String
    ) : FeedbackUiState
}