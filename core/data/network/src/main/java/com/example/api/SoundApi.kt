package com.example.api

import com.example.api.dto.SoundProcessingTaskResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SoundApi {

    @Multipart
    @POST(".")
    suspend fun createSoundProcessingTask(
        @Part file: MultipartBody.Part,
        @Part("text_id") textId: RequestBody
    ): SoundProcessingTaskResponse
}