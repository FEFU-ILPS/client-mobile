package com.example.api.dto

data class FeedbackDto(
    val id: String,
    val result:String,
    val mistakes: List<UserMistakeDto>,
    val accuracy: Double
)