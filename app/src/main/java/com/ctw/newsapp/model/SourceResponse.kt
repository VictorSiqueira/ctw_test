package com.ctw.newsapp.model

import kotlinx.serialization.Serializable

@Serializable
data class SourceResponse(
    val id: String?,
    val name: String
)