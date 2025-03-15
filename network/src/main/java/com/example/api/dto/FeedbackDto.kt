package com.example.api.dto

data class FeedbackDto(
    val id: String,
    val userMistakes: List<UserMistakeDto>,
    val mark: String,
    val accuracy: Double,
    val text: FeedBackTextDto
)