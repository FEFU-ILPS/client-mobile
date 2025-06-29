package com.example.feedback.model

import androidx.compose.ui.graphics.Color

enum class UserMistakeType(val code: String, val color: Color) {
    REPLACEMENT("Replacement", Color(0xFFBFB500)),
    INSERTION("Insertion", Color(0xFFFF7A22)),
    DELETION("Deletion", Color(0xFFFF0000));

    companion object {
        fun byCode(code: String): UserMistakeType {
            return values().firstOrNull { it.code == code }
                ?: throw IllegalArgumentException("Unknown UserMistakeType: $code")
        }
    }
}
