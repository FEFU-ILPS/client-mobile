package com.example.api.dto

import com.google.gson.annotations.SerializedName

data class ExerciseResponseDto(
    val item: ItemDto,
    @SerializedName("embedded")
    val embed: ExerciseEmbedDto
)
