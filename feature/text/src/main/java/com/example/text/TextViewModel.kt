package com.example.text

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.TextApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class TextViewModel @Inject constructor(
    private val textApi: TextApi,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow<TextUiState>(TextUiState.Loading)
    val uiState: StateFlow<TextUiState> = _uiState
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

    fun stopRecording() {
        recorder.stop()
        isRecording.value = false
    }
}