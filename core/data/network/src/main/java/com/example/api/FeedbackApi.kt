package com.example.api

import com.example.api.dto.FeedbackCreationResponseDto
import com.example.api.dto.FeedbackDto
import com.example.api.dto.FeedbackHistoryItemDto
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FeedbackApi {

    @Multipart
    @POST("feedbacks")
    suspend fun createFeedback(@Part file: MultipartBody.Part): FeedbackCreationResponseDto

    @GET("feedbacks/{id}")
    suspend fun getFeedback(@Path("id") id: String): FeedbackDto

    @GET("feedbacks")
    suspend fun getFeedbackHistory(): List<FeedbackHistoryItemDto>
}