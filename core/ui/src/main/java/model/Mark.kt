package com.google.samples.modularization.ui.model

enum class Mark(name: String) {
    EXCELLENT("Правильно"),
    NEEDS_IMPROVEMENT("Требует улучшений"),
    BAD("Плохо"),
    WRONG("Неверно");

    companion object {
        fun markOf(accuracy: Double): Mark {
            return when {
                accuracy >= 80 -> EXCELLENT
                accuracy in 50.0..79.0 -> NEEDS_IMPROVEMENT
                accuracy in 30.0..49.0 -> BAD
                else -> WRONG
            }
        }
    }
}