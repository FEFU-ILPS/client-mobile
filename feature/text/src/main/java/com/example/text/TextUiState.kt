package com.example.text

import com.example.api.dto.ExerciseResponseDto

sealed interface TextUiState {
    object Loading : TextUiState

    data class Error(
        val message: String
    ) : TextUiState

    data class Success(
        val exerciseResponseDto: ExerciseResponseDto
    ) : TextUiState
}