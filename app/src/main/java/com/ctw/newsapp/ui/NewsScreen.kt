package com.ctw.newsapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.ctw.newsapp.model.ArticleResponse
import com.ctw.newsapp.model.SourceResponse
import com.ctw.newsapp.viewmodel.NewsViewModel
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
fun NewsScreen(
    vm: NewsViewModel = viewModel(),
    onArticleClick: (ArticleResponse) -> Unit
) {
    val state by vm::state

    LaunchedEffect(Unit) {
        vm.loadNews()
    }

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val providerName = state.newsResponse?.articles
            ?.firstOrNull()
            ?.source
            ?.name ?: "News"

        Text(
            text = providerName,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when {
            state.isLoading -> CircularProgressIndicator()
            state.errorMessage != null -> Text(
                "Error: ${state.errorMessage}",
                color = MaterialTheme.colorScheme.error
            )

            else -> {
                val sortedArticles = state.newsResponse?.articles?.sortedByDescending { article ->
                    parseDate(article.publishedAt)
                } ?: emptyList()

                LazyColumn {
                    items(sortedArticles) { article ->
                        ArticleItem(article = article, onClick = { onArticleClick(article) })
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ArticleItem(article: ArticleResponse, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Text(article.title, style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        article.urlToImage?.let { url ->
            Image(
                painter = rememberAsyncImagePainter(model = url),
                contentDescription = article.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

fun parseDate(dateString: String): OffsetDateTime {
    return try {
        OffsetDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    } catch (_: Exception) {
        OffsetDateTime.MIN
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewArticleItem() {
    val article = ArticleResponse(
        title = "Example Title",
        description = "Example description",
        content = "Example article content.",
        urlToImage = null,
        publishedAt = "2023-01-01T00:00:00Z",
        source = SourceResponse("1", "Example Source"),
        author = "John Doe",
        url = "https://example.com/article"
    )
    ArticleItem(article = article, onClick = {})
}

@Preview(showBackground = true)
@Composable
fun PreviewNewsScreen() {
    Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
        PreviewArticleItem()
    }
}
