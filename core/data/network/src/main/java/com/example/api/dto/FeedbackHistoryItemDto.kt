package com.example.api.dto

import com.google.gson.annotations.SerializedName

data class FeedbackHistoryItemDto(
    val id: String,
    @SerializedName("created_at")
    val timestamp: String,
    val title: String,
    val accuracy: Double
)