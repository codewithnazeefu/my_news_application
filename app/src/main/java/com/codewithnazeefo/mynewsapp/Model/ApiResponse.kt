package com.codewithnazeefo.mynewsapp.Model

data class ApiResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)