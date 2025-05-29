package com.example.inventory.data

import android.content.Context
import com.example.favoritebooksapp.data.BooksDatabase
import com.example.favoritebooksapp.data.FavoritesRepository

class AppDataContainer(private val context: Context)  {

    val itemsRepository: FavoritesRepository by lazy {
        FavoritesRepository(BooksDatabase.getDatabase(context).favoriteBookDao())
    }
}