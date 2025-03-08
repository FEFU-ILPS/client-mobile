package com.example.api

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FeedbackApi {

    @Multipart
    @POST("/")
    fun getFeedback(@Part file: MultipartBody.Part)
}