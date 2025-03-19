package com.example.feedback_history

import com.example.api.dto.FeedbackHistoryItemDto

sealed interface FeedbackHistoryUiState {
    object Loading : FeedbackHistoryUiState

    data class Error(
        val message: String
    ) : FeedbackHistoryUiState

    data class Success(
        val feedbackHistory: List<FeedbackHistoryItemDto>
    ) : FeedbackHistoryUiState

}