package com.example.text

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.api.dto.TextDto
import com.google.samples.modularization.ui.Error
import com.google.samples.modularization.ui.Loading

@Composable
fun TextRoute(
    onNavigateBack: () -> Unit,
    viewModel: TextViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    val recordAudioPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewModel.startRecording(context)
            } else {
                Toast.makeText(
                    context,
                    "Необходимо предоставить доступ к микрофону",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    TextScreen(
        state = state,
        onRetry = {
            viewModel.retry()
        },
        onRecord = {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                viewModel.startRecording(context)
            } else {
                recordAudioPermissionResultLauncher.launch(
                    Manifest.permission.RECORD_AUDIO
                )
            }

        },
        onStopRecording = {
            viewModel.stopRecording()
        },
        onNavigateBack = onNavigateBack,
        isRecording = viewModel.isRecording.value,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
internal fun TextScreen(
    state: TextUiState,
    isRecording: Boolean,
    onRecord: () -> Unit,
    onRetry: () -> Unit,
    onNavigateBack: () -> Unit,
    onStopRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = onNavigateBack,
                content = {
                    Icon(
                        painter = rememberVectorPainter(Icons.Default.ArrowBack),
                        contentDescription = "arrow back icon"
                    )
                },
                modifier = Modifier.align(Alignment.TopStart)
            )
        }

        when (state) {
            is TextUiState.Error -> {
                Error(
                    cause = "Не удалось загрузить текст",
                    onRetry = onRetry
                )
            }

            TextUiState.Loading -> Loading()

            is TextUiState.Success -> Content(
                text = state.text,
                isRecording = isRecording,
                onRecord = onRecord,
                onStopRecording = onStopRecording,
                modifier = modifier
            )
        }
    }

}

@Composable
internal fun Content(
    text: TextDto,
    isRecording: Boolean,
    modifier: Modifier = Modifier,
    onRecord: () -> Unit,
    onStopRecording: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(text = text.title, fontWeight = FontWeight.Bold)
        Text(text = text.text)
        Box(modifier = Modifier.fillMaxSize()) {
            if (isRecording) {
                IconButton(
                    onClick = onStopRecording,
                    content = {
                        Icon(
                            painter = rememberVectorPainter(ImageVector.vectorResource(R.drawable.stop_record)),
                            contentDescription = "stop recording icon"
                        )
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            } else {
                IconButton(
                    onClick = onRecord,
                    content = {
                        Icon(
                            painter = rememberVectorPainter(ImageVector.vectorResource(R.drawable.mic_icon)),
                            contentDescription = "microphone icon"
                        )
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }

        }
    }

}