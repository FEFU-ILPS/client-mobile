package com.example.text

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.FeedbackApi
import com.example.api.TextApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class TextViewModel @Inject constructor(
    private val textApi: TextApi,
    private val feedbackApi: FeedbackApi,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow<TextUiState>(TextUiState.Loading)
    val uiState: StateFlow<TextUiState> = _uiState
    private val _feedbackUiState = MutableStateFlow<FeedbackUiState>(FeedbackUiState.NotSet)
    val feedbackUiState: StateFlow<FeedbackUiState> = _feedbackUiState
    private val textId = savedStateHandle.get<String>("textId")
    private val recorder = PcmAudioRecorder()
    val isRecording = mutableStateOf(false)

    init {
        loadText()
    }

    private fun loadText() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = textApi.getText(textId!!)
                _uiState.value = TextUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = TextUiState.Error(e.message.toString())
            }
        }
    }

    fun retry() {
        _uiState.value = TextUiState.Loading
        loadText()
    }

    fun startRecording(context: Context) {
        val audioFile = File(context.cacheDir, "audio_record.pcm")
        isRecording.value = true
        recorder.start(audioFile)
    }

    fun stopRecording(context: Context) {
        recorder.stop()
        isRecording.value = false
        sendFile(context)
    }

    private fun sendFile(context: Context) {
        _feedbackUiState.value = FeedbackUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val audioFile = File(context.cacheDir, "audio_record.pcm")
            try {
                val feedbackId = feedbackApi.createFeedback(
                    MultipartBody.Part.createFormData(
                        "audio",
                        audioFile.name,
                        audioFile.asRequestBody()
                    )
                ).id
                _feedbackUiState.value = FeedbackUiState.Success(feedbackId)
            } catch (e: Exception) {
                _feedbackUiState.value = FeedbackUiState.Error(e.message.toString())
            }
        }
    }

    fun retryFileUploading(context: Context) {
        sendFile(context)
    }
}