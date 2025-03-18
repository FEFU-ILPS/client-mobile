package com.example.sign_up

sealed interface SignUpUiState {

    object Loading : SignUpUiState

    object NotSet : SignUpUiState

    data class Error(
        val message: String
    ) : SignUpUiState

    object Success : SignUpUiState
}