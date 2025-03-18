package com.example.sign_in

sealed interface SignInUiState {

    object Loading : SignInUiState

    object NotSet : SignInUiState

    data class Error(
        val message: String
    ) : SignInUiState

    object Success : SignInUiState
}