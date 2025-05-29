package com.example.favoritebooksapp

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.favoritebooksapp.ui.viewModel.BooksViewModel
import com.example.favoritebooksapp.ui.viewModel.FavoritesViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            BooksViewModel()
        }
        initializer {
            FavoritesViewModel(favoriteBooksApplication().container.itemsRepository)
        }
    }
}

fun CreationExtras.favoriteBooksApplication(): FavoriteBooksApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as FavoriteBooksApplication)