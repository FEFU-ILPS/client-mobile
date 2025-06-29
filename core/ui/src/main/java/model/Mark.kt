package com.google.samples.modularization.ui.model

enum class Mark(val code: String) {
    EXCELLENT("Правильно"),
    NEEDS_IMPROVEMENT("Требует улучшений"),
    BAD("Плохо"),
    WRONG("Неверно");

    companion object {
        fun markOf(accuracy: Double): Mark {
            return when {
                accuracy >= 80 -> EXCELLENT
                accuracy >= 50 -> NEEDS_IMPROVEMENT
                accuracy >= 30 -> BAD
                else -> WRONG
            }
        }
    }
}