package com.example.text_list

import com.example.api.dto.TextListItemDto

sealed interface TextListUiState {
    object Loading : TextListUiState

    data class Error(
        val message: String
    ) : TextListUiState

    data class Success(
        val texts: List<TextListItemDto>
    ) : TextListUiState
}