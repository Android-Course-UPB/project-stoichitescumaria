package com.example.favoritebooksapp.model
import kotlinx.serialization.Serializable

@Serializable
data class BooksResponse(
    val items: List<BookItem>
)