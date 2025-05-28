package com.example.text

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onGoToFeedbackScreen: (String) -> Unit,
    viewModel: TextViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    val feedbackState by viewModel.feedbackUiState.collectAsState()

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
        onStopRecording = { title, textId ->
            viewModel.stopRecording(context = context, title = title, textId = textId)
        },
        onNavigateBack = onNavigateBack,
        isRecording = viewModel.isRecording.value,
        modifier = Modifier.padding(16.dp),
        onGoToFeedbackScreen = { id -> onGoToFeedbackScreen(id) },
        feedbackUiState = feedbackState,
        onRetryAudioUploading = { title, textId ->
            viewModel.retryFileUploading(
                context = context,
                title = title,
                textId = textId
            )
        }
    )
}

@Composable
internal fun TextScreen(
    state: TextUiState,
    isRecording: Boolean,
    onRecord: () -> Unit,
    onRetry: () -> Unit,
    onNavigateBack: () -> Unit,
    onStopRecording: (title: String, textId: String) -> Unit,
    modifier: Modifier = Modifier,
    onGoToFeedbackScreen: (String) -> Unit,
    onRetryAudioUploading: (title: String, textId: String) -> Unit,
    feedbackUiState: FeedbackUiState
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TopNavigation(
            onNavigateBack = onNavigateBack,
            modifier = Modifier.fillMaxWidth(),
            title = "Чтение текста"
        )

        when (state) {
            is TextUiState.Error -> {
                Error(
                    cause = "Не удалось загрузить текст",
                    onRetry = onRetry,
                    modifier = Modifier.fillMaxSize()
                )
            }

            TextUiState.Loading -> Loading(modifier = Modifier.fillMaxSize())

            is TextUiState.Success -> Content(
                text = state.exerciseResponseDto.embed.text,
                isRecording = isRecording,
                onRecord = onRecord,
                onStopRecording = onStopRecording,
                modifier = modifier,
                onGoToFeedbackScreen = onGoToFeedbackScreen,
                onRetryAudioUploading = onRetryAudioUploading,
                feedbackUiState = feedbackUiState
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
    onStopRecording: (title: String, textId: String) -> Unit,
    onGoToFeedbackScreen: (String) -> Unit,
    onRetryAudioUploading: (title: String, textId: String) -> Unit,
    feedbackUiState: FeedbackUiState
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(text = text.title, fontWeight = FontWeight.Bold)
        Text(text = text.value)
        Text(text = text.transcription, color = Color.Gray)
        RecordWidget(
            isRecording = isRecording,
            modifier = Modifier.fillMaxSize(),
            feedbackUiState = feedbackUiState,
            onRecord = onRecord,
            onStopRecording = { onStopRecording(text.title, text.id) },
            onRetryAudioUploading = { onRetryAudioUploading(text.title, text.id) },
            onGoToFeedbackScreen = onGoToFeedbackScreen
        )
    }
}

@Composable
fun RecordWidget(
    isRecording: Boolean,
    modifier: Modifier = Modifier,
    feedbackUiState: FeedbackUiState,
    onRecord: () -> Unit,
    onRetryAudioUploading: () -> Unit,
    onStopRecording: () -> Unit,
    onGoToFeedbackScreen: (String) -> Unit
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isRecording) {
                IconButton(
                    onClick = onStopRecording,
                    content = {
                        Icon(
                            painter = rememberVectorPainter(ImageVector.vectorResource(R.drawable.stop_record)),
                            contentDescription = "stop recording icon"
                        )
                    }
                )
            } else {
                IconButton(
                    onClick = onRecord,
                    enabled = feedbackUiState !is FeedbackUiState.Loading,
                    content = {
                        Icon(
                            painter = rememberVectorPainter(ImageVector.vectorResource(R.drawable.mic_icon)),
                            contentDescription = "microphone icon"
                        )
                    }
                )
            }
            when (feedbackUiState) {
                is FeedbackUiState.Error -> Error(
                    cause = "Не удалось отправить аудио",
                    onRetry = onRetryAudioUploading,
                    modifier = Modifier.fillMaxWidth()
                )

                FeedbackUiState.Loading -> Loading(modifier = Modifier.fillMaxWidth())
                FeedbackUiState.NotSet -> {
                    Text(text = "Прочитайте текст, чтобы увидеть результаты")
                }

                is FeedbackUiState.Success -> Button(
                    onClick = { onGoToFeedbackScreen(feedbackUiState.feedbackId) },
                    content = {
                        Text(text = "Смотреть результаты")
                    }
                )
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