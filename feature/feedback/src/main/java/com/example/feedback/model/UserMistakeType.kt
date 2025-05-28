package com.example.feedback.model

enum class UserMistakeType(val code: String) {
    REPLACEMENT("Replacement"),
    INSERTION("Insertion"),
    DELETION("Deletion");

    companion object {
        fun byCode(code: String): UserMistakeType {
            return values().firstOrNull { it.code == code }
                ?: throw IllegalArgumentException("Unknown UserMistakeType: $code")
        }
    }
}
