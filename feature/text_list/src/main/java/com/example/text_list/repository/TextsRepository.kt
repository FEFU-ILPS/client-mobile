package com.example.text_list.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.api.TextApi
import com.example.api.dto.TextListItemDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TextsRepository @Inject constructor(
    private val textApi: TextApi
) {
    fun getTexts(): Flow<PagingData<TextListItemDto>> {
        return Pager(
            PagingConfig(pageSize = 50)
        ) {
            TextsPagingSource(textApi)
        }.flow
    }
}