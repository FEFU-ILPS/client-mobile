package com.example.api.dto

data class FeedbackResponse(
    val item: FeedbackDto,
    val embedded: FeedbackEmbedDto
)