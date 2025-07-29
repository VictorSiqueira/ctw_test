package com.ctw.newsapp.repository

import com.ctw.newsapp.model.NewsResponse
import com.ctw.newsapp.network.NewsApi


class NewsRepository(
    private val api: NewsApi = NewsApi()
) {
    suspend fun getNews(): NewsResponse {
        return api.fetchTopHeadlines()
    }
}
