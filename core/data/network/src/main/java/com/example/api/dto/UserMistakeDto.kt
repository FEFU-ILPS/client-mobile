package com.example.api.dto

data class UserMistakeDto(
    val position: Int,
    val reference: String,
    val actual: String,
    val type: String
)