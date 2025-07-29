package com.ctw.newsapp.ui

import app.cash.paparazzi.Paparazzi
import androidx.compose.material3.MaterialTheme
import com.ctw.newsapp.model.ArticleResponse
import com.ctw.newsapp.model.SourceResponse
import org.junit.Rule
import org.junit.Test

class ArticleScreenPaparazziTest {
    @get:Rule
    val paparazzi = Paparazzi()

    private val article = ArticleResponse(
        title = "Example Title",
        description = "Example description for the article.",
        content = "Full content of the example article.",
        urlToImage = null,
        publishedAt = "2023-01-01T00:00:00Z",
        source = SourceResponse("1", "Example Source"),
        author = "John Doe",
        url = "https://example.com/article"
    )

    @Test
    fun articleDetailScreen_snapshot() {
        paparazzi.snapshot {
            MaterialTheme {
                ArticleDetailScreen(article = article, onBack = {})
            }
        }
    }

    @Test
    fun articleItem_snapshot() {
        paparazzi.snapshot {
            MaterialTheme {
                ArticleItem(article = article, onClick = {})
            }
        }
    }
}

