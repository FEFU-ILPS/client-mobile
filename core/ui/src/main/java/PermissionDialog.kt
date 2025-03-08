package com.google.samples.modularization.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PermissionDialog(
    text: String,
    title: String,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onOkClick()
                }
            ) {
                Text(text = "Ok")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onGoToAppSettingsClick()
                }
            ) {
                Text(text = "Dismiss")
            }
        },

        title = {
            Text(text = title)
        },
        text = {
            Text(
                text = text
            )
        },
        modifier = modifier
    )
}