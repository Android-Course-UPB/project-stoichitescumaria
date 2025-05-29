package com.example.favoritebooksapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import com.example.favoritebooksapp.ui.viewModel.FavoritesUiState
import com.example.favoritebooksapp.ui.viewModel.FavoritesViewModel

object FavoriteBooksDestination : NavigationDestination {
    override val route = "favorite_books"
    override val titleRes = R.string.favorite_books
}

@Composable
fun FavoriteBooksScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    navigateBack: () -> Unit,
    navigateToDetails: (String) -> Unit,
    favoritesViewModel: FavoritesViewModel
) {
    val favoritesUiState by favoritesViewModel.favoritesUiState

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(FavoriteBooksDestination.titleRes),
                showBackButton = true,
                isDarkTheme = isDarkTheme,
                onBack = navigateBack,
                onToggleTheme = onToggleTheme
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (favoritesUiState) {
                is FavoritesUiState.Loading -> LoadingScreen()
                is FavoritesUiState.Error -> ErrorScreen()
                is FavoritesUiState.Success -> {
                    val favoriteBooks = (favoritesUiState as FavoritesUiState.Success).favoriteBooks
                    if (favoriteBooks.isEmpty()) {
                        Text(
                            text = "No favorite books.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            contentPadding = innerPadding,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            items(favoriteBooks, key = { it.id }) { book ->
                                FavoriteBookCard(
                                    book = book,
                                    isFavorite = true,
                                    onFavoriteClick = { favoritesViewModel.toggleFavorite(book.id) },
                                    navigateToDetails = navigateToDetails
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteBookCard(
    book: BookItem,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    navigateToDetails: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable {
                navigateToDetails(book.id)
                       },
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