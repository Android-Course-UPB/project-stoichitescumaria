package com.example.favoritebooksapp.model
import kotlinx.serialization.Serializable

@Serializable
data class BookItem(
    val id: String,
    val volumeInfo: VolumeInfo
)
