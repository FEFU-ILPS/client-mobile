package com.example.feedback

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.example.api.dto.TextDto
import com.example.api.dto.UserMistakeDto
import com.example.feedback.model.UserMistakeType
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
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
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
                feedback = state.response.item,
                modifier = modifier,
                text = state.response.embedded.text
            )
        }
    }
}

@Composable
internal fun Content(
    feedback: FeedbackDto,
    text: TextDto,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        item {
            Text(text = text.title, fontWeight = FontWeight.Bold)
        }
        item {
            Text(text = text.value)
        }
        item {
            val referenceMistakes = feedback.mistakes.filter {
                it.type != UserMistakeType.INSERTION.code && it.reference != null
            }
            val referenceMap = referenceMistakes.associate {
                it.reference!!.position to UserMistakeType.byCode(it.type)
            }
            ColoredText(
                text = text.transcription,
                indexToMistakeType = referenceMap
            )
        }
        item {
            Text(text = "Результат:")
        }
        item {
            val insertionMistakes = feedback.mistakes.filter {
                it.type != UserMistakeType.DELETION.code && it.actual != null
            }
            val insertionMap = insertionMistakes.associate {
                it.actual!!.position to UserMistakeType.byCode(it.type)
            }
            ColoredText(
                text = feedback.result,
                indexToMistakeType = insertionMap
            )
        }
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Оценка:", modifier = Modifier.padding(8.dp))
                PronunciationMarkText(feedback.accuracy)
            }

        }
        userMistakesWidget(
            feedback.mistakes.sortedBy {
                (it.reference ?: it.actual)?.position
            }
        )
    }
}


@Composable
fun ColoredText(text: String, indexToMistakeType: Map<Int, UserMistakeType>) {
    val annotatedString = buildAnnotatedString {
        val words = text.split(" ")
        words.forEachIndexed { index, word ->
            val mistakeType = indexToMistakeType[index]
            if (mistakeType != null) {
                pushStyle(SpanStyle(color = mistakeType.color))
            }
            append(word)
            if (mistakeType != null) {
                pop()
            }
            if (index != words.lastIndex) {
                append(" ")
            }
        }
    }

    Text(text = annotatedString)
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
        val mistakeType = UserMistakeType.byCode(userMistake.type)
        when (mistakeType) {
            UserMistakeType.REPLACEMENT -> {
                Text(text = "Нужно было сказать:")
                Text(text = userMistake.reference?.value ?: "", color = Color.Green)
                Text(text = "Вы сказали:")
                Text(text = userMistake.actual?.value ?: "", color = Color.Red)
            }

            UserMistakeType.INSERTION -> {
                Text(text = "Лишний звук:")
                Text(text = userMistake.actual?.value ?: "", color = Color.Green)
            }

            UserMistakeType.DELETION -> {
                Text(text = "Не было сказано:")
                Text(text = userMistake.reference?.value ?: "", color = Color.Green)
            }
        }

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

fun LazyListScope.userMistakesWidget(
    userMistakes: List<UserMistakeDto>
) {
    items(
        items = userMistakes
    ) { userMistake ->
        UserMistakeItem(userMistake, modifier = Modifier.horizontalScroll(rememberScrollState()))
    }
}