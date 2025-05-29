package com.example.favoritebooksapp.ui.screens

import androidx.benchmark.perfetto.ExperimentalPerfettoTraceProcessorApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.favoritebooksapp.R
import com.example.favoritebooksapp.model.BookItem
import com.example.favoritebooksapp.ui.components.ErrorScreen
import com.example.favoritebooksapp.ui.components.LoadingScreen
import com.example.favoritebooksapp.ui.components.TopBar
import com.example.favoritebooksapp.ui.navigation.NavigationDestination
import com.example.favoritebooksapp.ui.viewModel.BooksUiState
import com.example.favoritebooksapp.ui.viewModel.BooksViewModel
import com.example.favoritebooksapp.ui.viewModel.FavoritesViewModel

object AllBooksDestination : NavigationDestination {
    override val route = "all_books"
    override val titleRes = R.string.all_books
}

@Composable
fun AllBooksScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    navigateBack: () -> Unit,
    navigateToDetails: (String) -> Unit,
    booksViewModel: BooksViewModel,
    favoritesViewModel: FavoritesViewModel
) {
    favoritesViewModel.loadFavorites()
    val refreshTrigger = remember { mutableStateOf(0) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                refreshTrigger.value++
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.empty_string),
                showBackButton = true,
                isDarkTheme = isDarkTheme,
                onBack = navigateBack,
                onToggleTheme = onToggleTheme
            )
        }
    ) { innerPadding ->
        key(refreshTrigger.value) {
            AllBooksPage(
                booksViewModel = booksViewModel,
                favoritesViewModel = favoritesViewModel,
                innerPadding = innerPadding,
                navigateToDetails = navigateToDetails
            )
        }
    }
}

@Composable
fun AllBooksPage(
    booksViewModel: BooksViewModel,
    favoritesViewModel: FavoritesViewModel,
    innerPadding: PaddingValues,
    navigateToDetails: (String) -> Unit,
) {
    val booksUiState by booksViewModel.booksUiState
    val favoriteIds by favoritesViewModel.favoriteBookIds.collectAsState()

    when (booksUiState) {
        is BooksUiState.Loading -> LoadingScreen()
        is BooksUiState.Success -> BooksList(
            books = (booksUiState as BooksUiState.Success).books,
            contentPadding = innerPadding,
            modifier = Modifier,
            nextPage = { booksViewModel.nextPage() },
            prevPage = { booksViewModel.prevPage() },
            navigateToDetails = navigateToDetails,
            favoriteIds = favoriteIds,
            onToggleFavorite = { favoritesViewModel.toggleFavorite(it) },
            currentPage = booksViewModel.getPage()
        )
        is BooksUiState.Error -> ErrorScreen()
    }
}

@OptIn(ExperimentalPerfettoTraceProcessorApi::class)
@Composable
fun BooksList(
    books: List<BookItem>,
    contentPadding: PaddingValues,
    modifier: Modifier,
    nextPage: () -> Unit,
    prevPage: () -> Unit,
    navigateToDetails: (String) -> Unit,
    favoriteIds: Set<String>,
    onToggleFavorite: (String) -> Unit,
    currentPage: Int
) {

    fun onPreviousClick() {
        prevPage()
    }

    fun onNextClick() {
        nextPage()
    }

    Column(
        modifier = modifier
            .padding(contentPadding)
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(books, key = { it.id }) { book ->
                BookHorizontalCard(
                    book = book,
                    isFavorite = book.id in favoriteIds,
                    navigateToDetails = navigateToDetails,
                    onFavoriteClick = { onToggleFavorite(book.id) }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Previous",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .let {
                        if (currentPage > 1) it.clickable { onPreviousClick() } else it
                    }
            )
            Text(
                text = "Page $currentPage",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Next",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { onNextClick() }
            )
        }
    }
}

@Composable
fun BookHorizontalCard(
    book: BookItem,
    isFavorite: Boolean,
    navigateToDetails: (String) -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { navigateToDetails(book.id) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(book.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://"))
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.loading_img),
                error = painterResource(R.drawable.ic_broken_image),
                contentDescription = book.volumeInfo.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = book.volumeInfo.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = book.volumeInfo.authors?.joinToString(", ") ?: "Unknown author",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = book.volumeInfo.description?.take(100) ?: "No description",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
            }

            Icon(
                imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onFavoriteClick() },
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
