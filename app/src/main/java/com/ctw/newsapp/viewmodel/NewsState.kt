package com.ctw.newsapp.viewmodel

import com.ctw.newsapp.model.NewsResponse

data class NewsState(
    val isLoading: Boolean = false,
    val newsResponse: NewsResponse? = null,
    val errorMessage: String? = null
)
