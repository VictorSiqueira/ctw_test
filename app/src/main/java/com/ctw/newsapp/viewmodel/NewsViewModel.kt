package com.ctw.newsapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctw.newsapp.repository.NewsRepository
import kotlinx.coroutines.launch

private const val UNKNOWN_ERROR = "Unknown error"

class NewsViewModel(
    private val repository: NewsRepository = NewsRepository()
) : ViewModel() {

    var state by mutableStateOf(NewsState())
        private set

    fun loadNews() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null)
            try {
                val news = repository.getNews()
                state = state.copy(isLoading = false, newsResponse = news)
            } catch (e: Exception) {
                state =
                    state.copy(isLoading = false, errorMessage = e.message ?: UNKNOWN_ERROR)
            }
        }
    }
}