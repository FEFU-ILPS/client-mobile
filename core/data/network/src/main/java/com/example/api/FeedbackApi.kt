package com.example.api

import com.example.api.dto.FeedbackDto
import com.example.api.dto.FeedbackHistoryItemDto
import retrofit2.http.GET
import retrofit2.http.Path

interface FeedbackApi {
    @GET("feedbacks/{id}")
    suspend fun getFeedback(@Path("id") id: String): FeedbackDto

    @GET("feedbacks")
    suspend fun getFeedbackHistory(): List<FeedbackHistoryItemDto>
}