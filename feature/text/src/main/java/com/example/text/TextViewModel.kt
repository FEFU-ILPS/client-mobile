package com.example.text

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.SoundApi
import com.example.api.TextApi
import com.example.api.dto.SoundSseEventStatus
import com.example.api.sse.DefaultSoundTaskEventHandler
import com.example.data_store.AuthDataStoreRepository
import com.launchdarkly.eventsource.EventSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class TextViewModel @Inject constructor(
    private val textApi: TextApi,
    private val soundApi: SoundApi,
    private val authRepository: AuthDataStoreRepository,
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
                val soundTaskId = soundApi.createSoundProcessingTask(
                    MultipartBody.Part.createFormData(
                        "file",
                        audioFile.name,
                        audioFile.asRequestBody()
                    ),
                    textId!!.toRequestBody("text/plain".toMediaTypeOrNull())
                ).id
                val accessToken = authRepository.getAccessToken().first()
                var eventSource: EventSource? = null
                eventSource = DefaultSoundTaskEventHandler.startSseClient(
                    soundTaskId = soundTaskId,
                    accessToken = accessToken!!,
                    onSSEConnectionOpened = { Log.d("sse", "sse connection opened") },
                    onSSEConnectionClosed = { Log.d("sse", "sse connection closed") },
                    onSSEEventReceived = { eventName, messageEvent ->
                        Log.d("sse event recived:", messageEvent.toString())
                        if (messageEvent.status == SoundSseEventStatus.COMPLETED) {
                            //TODO достать айди фидбека
                            _feedbackUiState.value = FeedbackUiState.Success("1")
                            eventSource?.close()
                        }
                    },
                    onSSEError = { e ->
                        Log.e("sse failed", e.message.toString())
                        _feedbackUiState.value = FeedbackUiState.Error(e.message.toString())
                        eventSource?.close()
                    }
                )
            } catch (e: Exception) {
                Log.e("failed to send file", e.message.toString())
                _feedbackUiState.value = FeedbackUiState.Error(e.message.toString())
            }
        }
    }

    fun retryFileUploading(context: Context) {
        sendFile(context)
    }
}