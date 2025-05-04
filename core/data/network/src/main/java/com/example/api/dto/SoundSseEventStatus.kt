package com.example.api.dto

import com.google.gson.annotations.SerializedName

enum class SoundSseEventStatus(name: String) {

    @SerializedName("preprocessing")
    PREPROCESSING("preprocessing"),

    @SerializedName("transcribing")
    TRANSCRIBING("transcribing"),

    @SerializedName("completed")
    COMPLETED("completed")
}