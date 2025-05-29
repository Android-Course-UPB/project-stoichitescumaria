package com.example.favoritebooksapp.ui.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.favoritebooksapp.data.FavoritesRepository
import com.example.favoritebooksapp.model.BookItem
import com.example.favoritebooksapp.network.BooksApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.State

sealed interface FavoritesUiState {
    object Loading : FavoritesUiState
    data class Success(val favoriteBooks: List<BookItem>) : FavoritesUiState
    object Error : FavoritesUiState
}


class FavoritesViewModel(private val repository: FavoritesRepository) : ViewModel() {

    private val _favoritesUiState = mutableStateOf<FavoritesUiState>(FavoritesUiState.Loading)
    val favoritesUiState: State<FavoritesUiState> = _favoritesUiState

    private val _favoriteBookIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteBookIds: StateFlow<Set<String>> = _favoriteBookIds


    init {
        loadFavoriteBooks()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            val favorites = repository.getAllFavoriteIds()
            _favoriteBookIds.value = favorites.toSet()
        }
    }

    fun loadFavoriteBooks() {
        viewModelScope.launch {
            try {
                _favoritesUiState.value = FavoritesUiState.Loading
                val favorites = repository.getAllFavoriteIds()
                _favoriteBookIds.value = favorites.toSet()

                val books = favorites.mapNotNull { id ->
                    try {
                        BooksApi.retrofitService.getBookById(id)
                    } catch (e: Exception) {
                        null
                    }
                }

                _favoritesUiState.value = FavoritesUiState.Success(books)
            } catch (e: Exception) {
                e
                _favoritesUiState.value = FavoritesUiState.Error
            }
        }
    }

    fun toggleFavorite(bookId: String) {
        viewModelScope.launch {
            if (repository.isFavorite(bookId)) {
                repository.removeFavorite(bookId)
            } else {
                repository.addFavorite(bookId)
            }
            loadFavoriteBooks()
        }
    }

    fun isFavorite(bookId: String): Boolean {
        return _favoriteBookIds.value.contains(bookId)
    }
}