package com.example.favoritebooksapp.model

import kotlinx.serialization.Serializable

@Serializable
data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val imageLinks: ImageLinks?,
    val publisher: String?,
    val publishedDate: String?,
    val description: String?,
    val categories: List<String>?
)