package com.example.api.dto

import com.google.gson.annotations.SerializedName

data class ExerciseListItemDto(
    val id: String,
    val title: String,
    @SerializedName("seq_number")
    val number: Int,
    @SerializedName("preview_image")
    val preview: String,
    val tags: List<String>
)