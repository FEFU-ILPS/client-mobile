package com.example.api.dto

data class UserMistakeDto(
    val reference: MistakeDto?,
    val actual: MistakeDto?,
    val type: String
)