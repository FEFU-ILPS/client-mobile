package com.example.text_list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.api.dto.ExerciseListItemDto
import com.google.samples.modularization.ui.Error
import com.google.samples.modularization.ui.Loading


@Composable
fun TextListRoute(
    onSelectText: (String) -> Unit,
    viewModel: TextListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val state = viewModel.uiState.collectAsLazyPagingItems()
    TextListScreen(
        state = state,
        onSelectText = onSelectText,
        onRetry = { state.retry() },
        modifier = modifier
    )
}

@Composable
internal fun TextListScreen(
    state: LazyPagingItems<ExerciseListItemDto>,
    onSelectText: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Content(
        lazyPagingItems = state,
        onSelectText = onSelectText,
        modifier = modifier
    )
    state.apply {
        when {
            loadState.refresh is LoadState.Loading -> {
                Loading(modifier = Modifier.fillMaxSize())
            }

            loadState.refresh is LoadState.Error -> {
                Error(
                    cause = "Не удалось загрузить тексты",
                    onRetry = onRetry,
                    modifier = Modifier.fillMaxSize()
                )
            }

            loadState.append is LoadState.Loading -> {
                Loading(modifier = Modifier.fillMaxSize())
            }

            loadState.append is LoadState.Error -> {
                Error(
                    cause = "Не удалось загрузить тексты",
                    onRetry = onRetry,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
internal fun Content(
    lazyPagingItems: LazyPagingItems<ExerciseListItemDto>,
    onSelectText: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it.id }
        ) { index ->
            val textItem = lazyPagingItems[index]!!
            TextWidget(
                title = "${textItem.title} (#${textItem.number})",
                tags = textItem.tags,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable {
                        onSelectText(textItem.id)
                    }
            )
        }
    }

}

@Composable
internal fun TextWidget(
    title: String,
    tags: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold
        )
        ExerciseTagWidget(tags)
    }
}

@Composable
internal fun ExerciseTagWidget(
    tags: List<String>
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { tag ->
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFF3F51B5),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = tag,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}
