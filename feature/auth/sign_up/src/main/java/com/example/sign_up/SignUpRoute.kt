package com.example.sign_up

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.samples.modularization.ui.Loading
import com.google.samples.modularization.ui.TextInput

@Composable
fun SignUpRoute(
    onGoBack: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    SignUpScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        userNameField = viewModel.userName.value,
        passwordField = viewModel.password.value,
        isPasswordFieldVisible = viewModel.isPasswordVisible.value,
        onPasswordFieldChange = {
            viewModel.onPasswordChange(it)
        },
        onUserNameFieldChange = {
            viewModel.onUserNameChange(it)
        },
        onPasswordFieldVisibilityChange = {
            viewModel.isPasswordVisible.value = !viewModel.isPasswordVisible.value
        },
        repeatPasswordField = viewModel.repeatPassword.value,
        isRepeatPasswordFieldVisible = viewModel.isRepeatPasswordFieldVisible.value,
        onRepeatPasswordFieldChange = {
            viewModel.onRepeatPasswordChange(it)
        },
        onRepeatPasswordFieldVisibilityChange = {
            viewModel.isRepeatPasswordFieldVisible.value =
                !viewModel.isRepeatPasswordFieldVisible.value
        },
        emailField = viewModel.email.value,
        onEmailFieldChange = {
            viewModel.onEmailChange(it)
        },
        onRegister = {
            viewModel.register()
        },
        onGoBack = onGoBack,
        state = state,
        emailError = viewModel.emailError.value,
        passwordError = viewModel.passwordError.value,
        repeatPasswordError = viewModel.repeatPasswordError.value,
        userNameError = viewModel.userNameError.value,
    )
}

@Composable
internal fun SignUpScreen(
    modifier: Modifier = Modifier,
    userNameField: String,
    passwordField: String,
    isPasswordFieldVisible: Boolean,
    onPasswordFieldChange: (String) -> Unit,
    onUserNameFieldChange: (String) -> Unit,
    onPasswordFieldVisibilityChange: () -> Unit,
    repeatPasswordField: String,
    isRepeatPasswordFieldVisible: Boolean,
    onRepeatPasswordFieldChange: (String) -> Unit,
    onRepeatPasswordFieldVisibilityChange: () -> Unit,
    emailField: String,
    onEmailFieldChange: (String) -> Unit,
    onRegister: () -> Unit,
    onGoBack: () -> Unit,
    state: SignUpUiState,
    emailError: String?,
    passwordError: String?,
    repeatPasswordError: String?,
    userNameError: String?
) {
    val context = LocalContext.current
    when (state) {
        SignUpUiState.Loading -> Loading(modifier = Modifier.fillMaxSize())

        SignUpUiState.Success -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Пользователь успешно зарегистрирован", Toast.LENGTH_SHORT)
                    .show()
                onGoBack()
            }
        }

        else -> {
            Content(
                modifier = modifier,
                userNameField = userNameField,
                passwordField = passwordField,
                isPasswordFieldVisible = isPasswordFieldVisible,
                onPasswordFieldChange = onPasswordFieldChange,
                onUserNameFieldChange = onUserNameFieldChange,
                onPasswordFieldVisibilityChange = onPasswordFieldVisibilityChange,
                repeatPasswordField = repeatPasswordField,
                isRepeatPasswordFieldVisible = isRepeatPasswordFieldVisible,
                onRepeatPasswordFieldChange = onRepeatPasswordFieldChange,
                onRepeatPasswordFieldVisibilityChange = onRepeatPasswordFieldVisibilityChange,
                emailField = emailField,
                onEmailFieldChange = onEmailFieldChange,
                onRegister = onRegister,
                onGoBack = onGoBack,
                emailError = emailError,
                passwordError = passwordError,
                repeatPasswordError = repeatPasswordError,
                userNameError = userNameError
            )
        }
    }

}

@Composable
internal fun Content(
    modifier: Modifier = Modifier,
    userNameField: String,
    passwordField: String,
    isPasswordFieldVisible: Boolean,
    onPasswordFieldChange: (String) -> Unit,
    onUserNameFieldChange: (String) -> Unit,
    onPasswordFieldVisibilityChange: () -> Unit,
    repeatPasswordField: String,
    isRepeatPasswordFieldVisible: Boolean,
    onRepeatPasswordFieldChange: (String) -> Unit,
    onRepeatPasswordFieldVisibilityChange: () -> Unit,
    emailField: String,
    onEmailFieldChange: (String) -> Unit,
    onRegister: () -> Unit,
    onGoBack: () -> Unit,
    emailError: String?,
    passwordError: String?,
    repeatPasswordError: String?,
    userNameError: String?

) {
    Column(
        modifier = modifier
    ) {
        TopNavigation(
            onNavigateBack = onGoBack,
            title = "Регистрация"
        )
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {
                TextInput(
                    value = userNameField,
                    placeholder = "Имя пользователя",
                    modifier = Modifier
                        .fillMaxWidth(),
                    errorMsg = userNameError,
                    onValueChange = onUserNameFieldChange
                )
                TextInput(
                    value = emailField,
                    placeholder = "Электронная почта",
                    modifier = Modifier
                        .fillMaxWidth(),
                    errorMsg = emailError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    onValueChange = onEmailFieldChange,
                )
                TextInput(
                    value = passwordField,
                    placeholder = "Пароль",
                    modifier = Modifier
                        .fillMaxWidth(),
                    errorMsg = passwordError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = onPasswordFieldChange,
                    visualTransformation = if (!isPasswordFieldVisible) {
                        PasswordVisualTransformation()
                    } else {
                        VisualTransformation.None
                    },
                    trailingIcon = {
                        val image = if (isPasswordFieldVisible) {
                            R.drawable.visible_icon
                        } else {
                            R.drawable.visibility_off_icon
                        }
                        IconButton(onClick = onPasswordFieldVisibilityChange) {
                            Icon(painter = painterResource(id = image), contentDescription = null)
                        }
                    }
                )

                TextInput(
                    value = repeatPasswordField,
                    placeholder = "Повторите пароль",
                    modifier = Modifier
                        .fillMaxWidth(),
                    errorMsg = repeatPasswordError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = onRepeatPasswordFieldChange,
                    visualTransformation = if (!isRepeatPasswordFieldVisible) {
                        PasswordVisualTransformation()
                    } else {
                        VisualTransformation.None
                    },
                    trailingIcon = {
                        val image = if (isRepeatPasswordFieldVisible) {
                            R.drawable.visible_icon
                        } else {
                            R.drawable.visibility_off_icon
                        }
                        IconButton(onClick = onRepeatPasswordFieldVisibilityChange) {
                            Icon(painter = painterResource(id = image), contentDescription = null)
                        }
                    }
                )

                Button(
                    onClick = onRegister,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Создать аккаунт")
                }
            }
        }
    }

}

@Composable
fun TopNavigation(
    onNavigateBack: () -> Unit,
    title: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.align(Alignment.TopStart),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                content = {
                    Icon(
                        painter = rememberVectorPainter(Icons.Default.ArrowBack),
                        contentDescription = "arrow back icon"
                    )
                },
            )
            Text(text = title)
        }
    }
}

