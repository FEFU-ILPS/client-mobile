package com.example.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.api.dto.FeedbackDto
import com.example.api.dto.UserMistakeDto
import com.google.samples.modularization.ui.Error
import com.google.samples.modularization.ui.Loading
import com.google.samples.modularization.ui.PronunciationMarkText

@Composable
fun FeedbackRoute(
    onNavigateBack: () -> Unit,
    viewModel: FeedbackViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    FeedbackScreen(
        state = state,
        onRetry = {
            viewModel.loadFeedback()
        },
        onNavigateBack = onNavigateBack,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
internal fun FeedbackScreen(
    state: FeedbackUiState,
    onRetry: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TopNavigation(
            onNavigateBack = onNavigateBack,
            title = "Отчет по произношению"
        )

        when (state) {
            is FeedbackUiState.Error -> {
                Error(
                    cause = "Не удалось загрузить отчет",
                    onRetry = onRetry,
                    modifier = Modifier.fillMaxSize()
                )
            }

            FeedbackUiState.Loading -> Loading(modifier = Modifier.fillMaxSize())

            is FeedbackUiState.Success -> Content(
                feedback = state.feedback,
                modifier = modifier,
            )
        }
    }
}

@Composable
internal fun Content(
    feedback: FeedbackDto,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(text = feedback.text.title, fontWeight = FontWeight.Bold)
            Text(text = feedback.text.text)
            val mistakeIndexes =
                feedback.userMistakes.map { userMistakeDto -> userMistakeDto.phonemePosition }
            ColoredText(text = feedback.text.phoneticTranscription, indexes = mistakeIndexes)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Оценка:", modifier = Modifier.padding(8.dp))
                PronunciationMarkText(feedback.mark)
            }

            UserMistakesWidget(
                feedback.userMistakes,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun ColoredText(text: String, indexes: List<Int>) {
    val annotatedString = buildAnnotatedString {
        for (i in text.indices) {
            val isColored = i in indexes
            if (isColored) {
                pushStyle(SpanStyle(color = Color.Red))
            }
            append(text[i])
            if (isColored) {
                pop()
            }
        }
    }
    Text(text = annotatedString)
}

@Composable
fun UserMistakesWidget(
    userMistakes: List<UserMistakeDto>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = userMistakes,
            key = { userMistake -> userMistake.phonemePosition }
        ) { userMistake ->
            UserMistakeItem(userMistake, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun UserMistakeItem(
    userMistake: UserMistakeDto,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Нужно было сказать:")
        Text(text = userMistake.requiredPhoneme, color = Color.Green)
        Text(text = "Вы сказали:")
        Text(text = userMistake.userPhoneme, color = Color.Red)
    }
}

@Composable
fun TopNavigation(
    onNavigateBack: () -> Unit,
    title: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.align(Alignment.TopStart),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                content = {
                    Icon(
                        painter = rememberVectorPainter(Icons.Default.ArrowBack),
                        contentDescription = "arrow back icon"
                    )
                },
            )
            Text(text = title)
        }
    }
}