package com.google.samples.modularization.ui.model

enum class PronunciationMark(val mark: String) {
    Excellent("Правильно"),
    NeedImprovement("Требует улучшений"),
    Bad("Плохо"),
    Wrong("Неверно");

    companion object {
        fun pronunciationMarkOf(mark: String): PronunciationMark? {
            return PronunciationMark.values()
                .find { pronunciationMark -> pronunciationMark.mark == mark }
        }
    }
}