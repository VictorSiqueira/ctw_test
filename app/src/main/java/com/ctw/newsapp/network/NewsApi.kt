package com.ctw.newsapp.network

import com.ctw.newsapp.BuildConfig
import com.ctw.newsapp.model.NewsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val HOST = "newsapi.org"

class NewsApi {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun fetchTopHeadlines(country: String = "us"): NewsResponse {
        return client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = HOST
                encodedPath = BuildConfig.NEWS_API_ENDPOINT
                parameters.append("country", country)
                parameters.append("apiKey", BuildConfig.NEWS_API_TOKEN)
            }
        }.body()
    }
}