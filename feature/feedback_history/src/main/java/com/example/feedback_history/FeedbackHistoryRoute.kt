package com.example.feedback_history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.api.dto.FeedbackHistoryItemDto
import com.google.samples.modularization.ui.Error
import com.google.samples.modularization.ui.Loading
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun FeedbackHistoryRoute(
    onGoToFeedback: (String) -> Unit,
    viewModel: FeedbackHistoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    FeedbackHistoryScreen(
        state = state,
        onSelectFeedback = onGoToFeedback,
        onRetry = { viewModel.retry() },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
internal fun FeedbackHistoryScreen(
    state: FeedbackHistoryUiState,
    onSelectFeedback: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        FeedbackHistoryUiState.Loading -> Loading(modifier = Modifier.fillMaxSize())

        is FeedbackHistoryUiState.Success -> Content(
            feedbackItems = state.feedbackHistory,
            onSelectFeedback = onSelectFeedback,
            modifier = modifier
        )

        is FeedbackHistoryUiState.Error -> Error(
            cause = "Не удалось загрузить тексты",
            onRetry = onRetry,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
internal fun Content(
    feedbackItems: List<FeedbackHistoryItemDto>,
    onSelectFeedback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (feedbackItems.isEmpty()) {
        Box(modifier = modifier) {
            Text(
                text = "Прочитайте текст, чтобы получить отчет по произношению",
                modifier = Modifier.align(Alignment.Center)
            )
        }

    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
        ) {
            items(
                items = feedbackItems,
                key = { item -> item.id }
            ) { feedbackItem ->
                FeedbackWidget(
                    textTitle = feedbackItem.textTitle,
                    mark = feedbackItem.mark,
                    timestamp = ZonedDateTime.parse(feedbackItem.timestamp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable {
                            onSelectFeedback(feedbackItem.id)
                        }
                )
            }
        }
    }


}

@Composable
internal fun FeedbackWidget(
    textTitle: String,
    mark: String,
    timestamp: ZonedDateTime,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.getDefault())
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = textTitle,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = timestamp.format(formatter),
                maxLines = 1,
                color = Color(0x6D000000)
            )
        }
        Text(
            text = mark,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }

}