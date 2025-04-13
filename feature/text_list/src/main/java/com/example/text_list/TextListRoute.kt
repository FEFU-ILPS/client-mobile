package com.example.text_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.api.dto.TextListItemDto
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
    state: LazyPagingItems<TextListItemDto>,
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
    lazyPagingItems: LazyPagingItems<TextListItemDto>,
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
                textItem.title,
                textItem.preview,
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
    preview: String,
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
        Text(
            text = preview,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color(0x6D000000)
        )
    }
}