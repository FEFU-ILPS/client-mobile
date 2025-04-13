package com.example.text_list.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.api.TextApi
import com.example.api.dto.TextListItemDto
import javax.inject.Inject

class TextsPagingSource @Inject constructor(
    private val textApi: TextApi
) : PagingSource<Int, TextListItemDto>() {

    override fun getRefreshKey(state: PagingState<Int, TextListItemDto>): Int? {
        return state.anchorPosition?.let { position ->
            val closestPage = state.closestPageToPosition(position)
            closestPage?.prevKey?.plus(state.config.pageSize)
                ?: closestPage?.nextKey?.minus(state.config.pageSize)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TextListItemDto> {
        val offset = params.key ?: 0
        return try {
            val limit = params.loadSize
            val response = textApi.getTexts(limit = limit, offset = offset)

            LoadResult.Page(
                data = response,
                prevKey = if (offset == 0) null else offset - limit,
                nextKey = if (response.isEmpty()) null else offset + limit
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
