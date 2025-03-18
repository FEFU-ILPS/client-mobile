package com.google.samples.modularization.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(
    value: String,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    placeholder: String,
    maxLines: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier,
    errorMsg: String? = null,
    keyboardOptions: KeyboardOptions? = null,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation? = null,
    trailingIcon: @Composable() (() -> Unit)? = null
) {

    val unfocusedBorderColor: Color = if (errorMsg==null) {
        Color.Blue
    } else {
        Color.Red
    }
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            maxLines = maxLines,
            modifier = Modifier
                .fillMaxWidth(),
            enabled = enabled,
            singleLine = singleLine,
            keyboardOptions = keyboardOptions ?: KeyboardOptions.Default,
            placeholder = {
                Text(text = placeholder)
            },
            visualTransformation = visualTransformation ?: VisualTransformation.None,
            trailingIcon = trailingIcon,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = unfocusedBorderColor,
                focusedBorderColor = unfocusedBorderColor,
                unfocusedLabelColor = unfocusedBorderColor,
                containerColor = Color.White
            )
        )
        if (errorMsg != null) {
            AnimatedVisibility(visible = errorMsg != null) {
                Text(text = errorMsg, color = unfocusedBorderColor)
            }
        }

    }
}