package com.google.samples.modularization.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.samples.modularization.ui.model.Mark

@Composable
fun PronunciationMarkText(accuracy: Double, modifier: Modifier = Modifier) {
    val mark = Mark.markOf(accuracy)
    when (mark) {
        Mark.EXCELLENT -> {
            Text(
                text = "${mark.name} (${accuracy}%)",
                color = Color.Green,
                modifier = modifier
            )
        }

        Mark.NEEDS_IMPROVEMENT -> {
            Text(
                text = "${mark.name} (${accuracy}%)",
                color = Color(0xFFFF9800),
                modifier = modifier
            )
        }

        Mark.BAD -> {
            Text(
                text = "${mark.name} (${accuracy}%)",
                color = Color(0xFFFF5722),
                modifier = modifier
            )
        }

        Mark.WRONG -> {
            Text(
                text = "${mark.name} (${accuracy}%)",
                color = Color.Red,
                modifier = modifier
            )
        }

        else -> {
            Text(text = "Ошибка", color = Color.Red, modifier = modifier)
        }
    }
}