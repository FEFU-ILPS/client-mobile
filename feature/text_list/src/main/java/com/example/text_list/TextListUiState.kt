package com.example.text_list

import androidx.paging.PagingData
import com.example.api.dto.ExerciseListItemDto

sealed interface TextListUiState {
    object Loading : TextListUiState

    data class Error(
        val message: String
    ) : TextListUiState

    data class Success(
        val texts: PagingData<ExerciseListItemDto>
    ) : TextListUiState
}