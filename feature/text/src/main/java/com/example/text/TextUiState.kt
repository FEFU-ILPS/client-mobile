package com.example.text

import com.example.api.dto.TextDto

sealed interface TextUiState {
    object Loading : TextUiState

    data class Error(
        val message: String
    ) : TextUiState

    data class Success(
        val text: TextDto
    ) : TextUiState
}