package com.example.api

import com.example.api.dto.FeedbackCreationResponseDto
import com.example.api.dto.FeedbackDto
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FeedbackApi {

    @Multipart
    @POST("/feedback")
    suspend fun createFeedback(@Part file: MultipartBody.Part): FeedbackCreationResponseDto

    @GET("/feedback/{id}")
    suspend fun getFeedback(@Path("id") id: String): FeedbackDto
}