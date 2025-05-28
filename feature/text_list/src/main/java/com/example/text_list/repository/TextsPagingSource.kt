package com.example.text_list.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.api.ExercisesApi
import com.example.api.dto.ExerciseListItemDto
import javax.inject.Inject


class TextsPagingSource @Inject constructor(
    private val exercisesApi: ExercisesApi
) : PagingSource<Int, ExerciseListItemDto>() {

    override fun getRefreshKey(state: PagingState<Int, ExerciseListItemDto>): Int? {
        return state.anchorPosition?.let { position ->
            val closestPage = state.closestPageToPosition(position)
            closestPage?.prevKey?.plus(1)
                ?: closestPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ExerciseListItemDto> {
        val page = params.key ?: 1
        return try {
            val size = params.loadSize
            val response = exercisesApi.getExercises(page = page, size = size)

            LoadResult.Page(
                data = response.items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.items.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.e("exercises", e.message.toString())
            LoadResult.Error(e)
        }
    }
}
