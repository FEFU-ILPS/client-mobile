package com.example.api.dto

import com.google.gson.annotations.SerializedName

data class ExerciseListItemDto(
    val id: String,
    val title: String,//todo rename
    @SerializedName("seq_number")
    val number: Int,
    @SerializedName("preview_image")
    val preview: String
)