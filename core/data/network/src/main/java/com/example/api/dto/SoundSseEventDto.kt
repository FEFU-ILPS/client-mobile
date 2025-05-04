package com.example.api.dto

import com.google.gson.annotations.SerializedName

data class SoundSseEventDto(
    val event: String,
    @SerializedName("task_id") val taskId: String,
    val status: SoundSseEventStatus,
    val retry: Long
)