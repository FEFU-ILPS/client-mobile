package com.example.sign_up

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.AuthApi
import com.example.api.dto.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authApi: AuthApi
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.NotSet)
    val uiState: StateFlow<SignUpUiState> = _uiState

    val password = mutableStateOf("")
    val isPasswordVisible = mutableStateOf(false)
    val userName = mutableStateOf("")
    val userNameError = mutableStateOf<String?>(null)

    val passwordError = mutableStateOf<String?>(null)

    val email = mutableStateOf("")
    val emailError = mutableStateOf<String?>(null)

    val isRepeatPasswordFieldVisible = mutableStateOf(false)
    val repeatPassword = mutableStateOf("")
    val repeatPasswordError = mutableStateOf<String?>(null)

    fun register() {
        if (validateUsername() && validateEmail() && validatePassword() && validateRepeatPassword()) {
            viewModelScope.launch(Dispatchers.IO) {
                _uiState.value = SignUpUiState.Loading
                try {
                    val registerRequest = RegisterRequest(
                        name = userName.value,
                        password = password.value,
                        email = email.value
                    )
                    authApi.register(registerRequest)
                    _uiState.value = SignUpUiState.Success
                } catch (e: Exception) {
                    _uiState.value = SignUpUiState.Error(e.message.toString())
                }
            }
        }
    }

    fun onPasswordChange(newValue: String) {
        password.value = newValue
        passwordError.value = null
    }

    fun onUserNameChange(newValue: String) {
        userName.value = newValue
        userNameError.value = null
    }

    fun onEmailChange(newValue: String) {
        email.value = newValue
        emailError.value = null
    }

    fun onRepeatPasswordChange(newValue: String) {
        repeatPassword.value = newValue
        repeatPasswordError.value = null
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

    private fun validateEmail(): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        return when {
            email.value.isEmpty() -> {
                emailError.value = "Email не может быть пустым"
                false
            }

            !email.value.matches(emailRegex.toRegex()) -> {
                emailError.value = "Некорректный формат email"
                false
            }

            else -> {
                emailError.value = null
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

    private fun validateRepeatPassword(): Boolean {
        return when {
            repeatPassword.value.isEmpty() -> {
                repeatPasswordError.value = "Повторите пароль"
                false
            }

            repeatPassword.value != password.value -> {
                repeatPasswordError.value = "Пароли не совпадают"
                false
            }

            else -> {
                repeatPasswordError.value = null
                true
            }
        }
    }
}