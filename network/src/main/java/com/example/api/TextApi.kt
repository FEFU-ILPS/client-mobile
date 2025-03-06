package com.example.api

import com.example.api.dto.TextListItemDto
import retrofit2.http.GET

interface TextApi {

    @GET("/texts")
    suspend fun getTexts(): List<TextListItemDto>
}