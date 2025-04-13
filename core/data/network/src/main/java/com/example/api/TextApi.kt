package com.example.api

import com.example.api.dto.TextDto
import com.example.api.dto.TextListItemDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TextApi {

    @GET("texts")
    suspend fun getTexts(
        @Query("limit") limit: Int,
        @Query("skip") offset: Int
    ): List<TextListItemDto>

    @GET("texts/{textId}")
    suspend fun getText(@Path("textId") textId: String): TextDto
}