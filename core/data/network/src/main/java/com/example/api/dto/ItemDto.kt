package com.example.api.dto

import com.google.gson.annotations.SerializedName

data class ItemDto(
    val id: String,
    @SerializedName("seq_number")
    val number: Int,
    val difficulty: Int,
    @SerializedName("text_id")
    val textId: String,
    val lang: String,
    val tags: List<String>
)