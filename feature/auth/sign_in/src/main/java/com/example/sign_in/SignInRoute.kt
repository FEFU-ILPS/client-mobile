package com.example.sign_in

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.samples.modularization.ui.Loading
import com.google.samples.modularization.ui.TextInput

@Composable
fun SignInRoute(
    onNavigateToTexts: () -> Unit,
    onGoToRegister: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkIsUserRemembered()
    }

    SignInScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        userNameField = viewModel.userName.value,
        passwordField = viewModel.password.value,
        isPasswordFieldVisible = viewModel.isPasswordVisible.value,
        isRememberMeChecked = viewModel.isRememberBtnClicked.value,
        onPasswordFieldChange = {
            viewModel.onPasswordChange(it)
        },
        onUserNameFieldChange = {
            viewModel.onUserNameChange(it)
        },
        onPasswordFieldVisibilityChange = {
            viewModel.isPasswordVisible.value = !viewModel.isPasswordVisible.value
        },
        onRememberMeCheckedChange = {
            viewModel.isRememberBtnClicked.value = it
        },
        onSignIn = {
            viewModel.signIn()
        },
        onNavigateToTexts = onNavigateToTexts,
        onGoToRegister = onGoToRegister,
        state = state,
        userNameError = viewModel.userNameError.value,
        passwordError = viewModel.passwordError.value
    )
}

@Composable
internal fun SignInScreen(
    modifier: Modifier = Modifier,
    userNameField: String,
    passwordField: String,
    isPasswordFieldVisible: Boolean,
    onPasswordFieldChange: (String) -> Unit,
    onUserNameFieldChange: (String) -> Unit,
    onPasswordFieldVisibilityChange: () -> Unit,
    isRememberMeChecked: Boolean,
    onRememberMeCheckedChange: (Boolean) -> Unit,
    onSignIn: () -> Unit,
    onGoToRegister: () -> Unit,
    onNavigateToTexts: () -> Unit,
    state: SignInUiState,
    userNameError: String?,
    passwordError: String?
) {
    when (state) {

        SignInUiState.Loading -> Loading(modifier = Modifier.fillMaxSize())

        SignInUiState.Success -> {
            LaunchedEffect(Unit) {
                onNavigateToTexts()
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
                isRememberMeChecked = isRememberMeChecked,
                onRememberMeCheckedChange = onRememberMeCheckedChange,
                onSignIn = onSignIn,
                onGoToRegister = onGoToRegister,
                userNameError = userNameError,
                passwordError = passwordError
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
    isRememberMeChecked: Boolean,
    onRememberMeCheckedChange: (Boolean) -> Unit,
    onSignIn: () -> Unit,
    onGoToRegister: () -> Unit,
    userNameError: String?,
    passwordError: String?

) {
    Box(
        modifier = modifier,
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
            RememberMeWidget(
                label = "Запомнить меня",
                checked = isRememberMeChecked,
                onCheckedChange = onRememberMeCheckedChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
            )
            Button(
                onClick = onSignIn,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Войти")
            }
            Button(
                onClick = onGoToRegister,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Создать аккаунт")
            }
        }
    }
}

@Composable
internal fun RememberMeWidget(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(text = label)
    }
}
