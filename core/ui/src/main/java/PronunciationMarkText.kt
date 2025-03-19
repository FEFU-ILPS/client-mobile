package com.google.samples.modularization.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.samples.modularization.ui.model.PronunciationMark

@Composable
fun PronunciationMarkText(mark: String, modifier: Modifier = Modifier) {
    when (val pronunciationMark = PronunciationMark.pronunciationMarkOf(mark)) {
        PronunciationMark.Excellent -> {
            Text(text = pronunciationMark.mark, color = Color.Green, modifier = modifier)
        }

        PronunciationMark.NeedImprovement -> {
            Text(text = pronunciationMark.mark, color = Color(0xFFFF9800), modifier = modifier)
        }

        PronunciationMark.Bad -> {
            Text(text = pronunciationMark.mark, color = Color(0xFFFF5722), modifier = modifier)
        }

        PronunciationMark.Wrong -> {
            Text(text = pronunciationMark.mark, color = Color.Red, modifier = modifier)
        }

        else -> {
            Text(text = "Ошибка", color = Color.Red, modifier = modifier)
        }
    }
}