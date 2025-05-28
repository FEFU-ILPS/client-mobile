package com.example.api

import com.example.api.dto.FeedbackHistoryItemDto
import com.example.api.dto.FeedbackResponse
import com.example.api.dto.SoundProcessingTaskResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface TasksApi {

    @Multipart
    @POST(".")
    suspend fun createSoundProcessingTask(
        @Part file: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("text_id") textId: RequestBody
    ): SoundProcessingTaskResponse

    @GET("{uuid}/embedded")
    suspend fun getTaskInfo(
        @Path("uuid") taskId: String,
        @Query("entities") embed: String = "text"
    ): FeedbackResponse

    @GET(".")
    suspend fun getFeedbackHistory(): List<FeedbackHistoryItemDto>

}