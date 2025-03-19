package com.example.sign_in

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.AuthApi
import com.example.api.dto.LoginRequest
import com.example.data_store.AuthDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authApi: AuthApi,
    private val authDataStoreRepository: AuthDataStoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignInUiState>(SignInUiState.NotSet)
    val uiState: StateFlow<SignInUiState> = _uiState

    val password = mutableStateOf("")
    val isPasswordVisible = mutableStateOf(false)
    val userName = mutableStateOf("")
    val isRememberBtnClicked = mutableStateOf(false)
    val userNameError = mutableStateOf<String?>(null)
    val passwordError = mutableStateOf<String?>(null)
    val isUserSignedIn = mutableStateOf(false)


    fun signIn() {
        viewModelScope.launch(Dispatchers.IO) {
            if (validateUsername() && validatePassword()) {
                _uiState.value = SignInUiState.Loading
                try {
                    val loginRequest =
                        LoginRequest(username = userName.value, password = password.value)
                    val loginResponse = authApi.login(loginRequest)
                    authDataStoreRepository.setAccessToken(loginResponse.accessToken)
                    if (isRememberBtnClicked.value) {
                        authDataStoreRepository.setUserRememberedFlag(isRememberBtnClicked.value)
                    }
                    _uiState.value = SignInUiState.Success
                } catch (e: Exception) {
                    _uiState.value = SignInUiState.Error(message = e.message.toString())
                }
            }
        }
    }

    fun checkIsUserRemembered() {
        viewModelScope.launch(Dispatchers.IO) {
            val isUserRemembered = authDataStoreRepository.getUserRememberedFlag().first()
            if (isUserRemembered == true) {
                isUserSignedIn.value = true
            }
        }
    }

    fun onPasswordChange(newValue: String) {
        password.value = newValue
    }

    fun onUserNameChange(newValue: String) {
        userName.value = newValue
    }

    private fun validateUsername(): Boolean {
        val usernameRegex = "^[a-zA-Z0-9_]{3,20}\$"
        return when {
            userName.value.isEmpty() -> {
                userNameError.value = "Имя пользователя не может быть пустым"
                false
            }

            !userName.value.matches(usernameRegex.toRegex()) -> {
                userNameError.value =
                    "Имя пользователя должно быть от 3 до 20 символов и содержать только буквы, цифры и _"
                false
            }

            else -> {
                userNameError.value = null
                true
            }
        }
    }

    private fun validatePassword(): Boolean {
        val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+!=])(?=\\S+$).{8,}$"
        return when {
            password.value.isEmpty() -> {
                passwordError.value = "Пароль не может быть пустым"
                false
            }

            !password.value.matches(passwordRegex.toRegex()) -> {
                passwordError.value =
                    "Пароль должен содержать минимум 8 символов, одну цифру, одну заглавную букву, одну строчную букву и один специальный символ"
                false
            }

            else -> {
                passwordError.value = null
                true
            }
        }
    }
}