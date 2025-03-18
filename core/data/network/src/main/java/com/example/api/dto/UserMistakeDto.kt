package com.example.api.dto

data class UserMistakeDto(
    val phonemePosition: Int,
    val requiredPhoneme: String,
    val userPhoneme: String
)