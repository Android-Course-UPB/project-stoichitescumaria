package com.example.favoritebooksapp.ui.viewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.favoritebooksapp.data.FavoritesRepository
import com.example.favoritebooksapp.model.BookItem
import com.example.favoritebooksapp.network.BooksApi
import kotlinx.coroutines.launch


sealed interface BooksUiState {
    data class Success(val books: List<BookItem>) : BooksUiState
    object Error : BooksUiState
    object Loading : BooksUiState
}

sealed interface BookDetailsUiState {
    object Loading : BookDetailsUiState
    data class Success(val book: BookItem) : BookDetailsUiState
    object Error : BookDetailsUiState
}

class BooksViewModel(
) : ViewModel() {

    private var _booksUiState = mutableStateOf<BooksUiState>(BooksUiState.Loading)
    var booksUiState: State<BooksUiState> = _booksUiState

    private val _bookDetailsUiState = mutableStateOf<BookDetailsUiState>(BookDetailsUiState.Loading)
    val bookDetailsUiState: State<BookDetailsUiState> = _bookDetailsUiState

    var title by mutableStateOf("a")
        private set

    var author by mutableStateOf("a")
        private set

    private var startIndex = 0
    private val maxResults = 5
    private var isLoading = false
    private val allBooks = mutableListOf<BookItem>()


    fun setSearchQuery(newTitle: String, newAuthor: String) {
        title = newTitle
        author = newAuthor
        startIndex = 0
        fetchBooks()
    }

    fun nextPage() {
        startIndex += maxResults
        _booksUiState.value = BooksUiState.Loading
        fetchBooks()
    }

    fun prevPage() {
        startIndex -= maxResults
        _booksUiState.value = BooksUiState.Loading
        fetchBooks()
    }


    fun fetchBooks() {
        if (isLoading) return
        isLoading = true

        allBooks.clear()
        viewModelScope.launch {
            try {
                val query = buildString {
                    if (title.isNotBlank()) append("intitle:$title ")
                    if (author.isNotBlank()) append("inauthor:$author")
                }
                val result = BooksApi.retrofitService.searchBooks(query, startIndex, maxResults)
                allBooks.addAll(result.items)
                _booksUiState.value = BooksUiState.Success(allBooks.toList())
            } catch (e: Exception) {
                e.toString()
                _booksUiState.value = BooksUiState.Error
            } finally {
                isLoading = false
            }
        }
    }

    fun loadBookDetails(bookId: String) {
        viewModelScope.launch {
            _bookDetailsUiState.value = BookDetailsUiState.Loading
            try {
                val book = BooksApi.retrofitService.getBookById(bookId)
                _bookDetailsUiState.value = BookDetailsUiState.Success(book)
            } catch (e: Exception) {
                _bookDetailsUiState.value = BookDetailsUiState.Error
            }
        }
    }

    fun getPage() : Int{
        return startIndex/5 + 1
    }

}
