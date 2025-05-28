package com.example.api.dto

import com.google.gson.annotations.SerializedName

enum class SoundSseEventStatus(name: String) {

    @SerializedName("created")
    CREATED("created"),

    @SerializedName("started")
    STARTED("started"),

    @SerializedName("preprocessing")
    PREPROCESSING("preprocessing"),

    @SerializedName("transcribing")
    TRANSCRIBING("transcribing"),

    @SerializedName("evaluating")
    EVALUATING("evaluating"),

    @SerializedName("completed")
    COMPLETED("completed"),

    @SerializedName("failed")
    FAILED("failed"),

    @SerializedName("unknown")
    UNKNOWN("unknown")
}