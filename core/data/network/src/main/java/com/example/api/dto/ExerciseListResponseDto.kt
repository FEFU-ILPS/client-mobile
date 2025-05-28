package com.example.api.dto

import com.google.gson.annotations.SerializedName

data class ExerciseListResponseDto(
    val items: List<ExerciseListItemDto>,
    val page: Int,
    val size: Int,
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)
