package com.example.api.dto

data class FeedbackHistoryItemDto(
    val id: String,
    val timestamp: String,
    val textTitle: String,
    val accuracy: Double,
    val mark: String
)