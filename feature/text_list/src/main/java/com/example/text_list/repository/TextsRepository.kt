package com.example.text_list.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.api.ExercisesApi
import com.example.api.dto.ExerciseListItemDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TextsRepository @Inject constructor(
    private val exercisesApi: ExercisesApi
) {
    fun getTexts(): Flow<PagingData<ExerciseListItemDto>> {
        return Pager(
            PagingConfig(pageSize = 50)
        ) {
            TextsPagingSource(exercisesApi)
        }.flow
    }
}