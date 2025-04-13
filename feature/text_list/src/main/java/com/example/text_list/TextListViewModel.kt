package com.example.text_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.api.dto.TextListItemDto
import com.example.text_list.repository.TextsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextListViewModel @Inject constructor(
    private val textsRepository: TextsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PagingData<TextListItemDto>>(PagingData.empty())
    val uiState: StateFlow<PagingData<TextListItemDto>> = _uiState

    init {
        loadTexts()
    }

    private fun loadTexts() {
        viewModelScope.launch(Dispatchers.IO) {
            textsRepository.getTexts()
                .cachedIn(viewModelScope)
                .collect { response ->
                    _uiState.value = response
                }
        }
    }
}
