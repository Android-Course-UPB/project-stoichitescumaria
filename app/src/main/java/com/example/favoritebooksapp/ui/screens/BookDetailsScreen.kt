import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.favoritebooksapp.AppViewModelProvider
import com.example.favoritebooksapp.R
import com.example.favoritebooksapp.model.BookItem
import com.example.favoritebooksapp.ui.components.ErrorScreen
import com.example.favoritebooksapp.ui.components.TopBar
import com.example.favoritebooksapp.ui.viewModel.BooksUiState
import com.example.favoritebooksapp.ui.viewModel.BooksViewModel
import com.example.favoritebooksapp.ui.viewModel.FavoritesViewModel
import androidx.compose.runtime.getValue
import com.example.favoritebooksapp.ui.components.LoadingScreen
import com.example.favoritebooksapp.ui.navigation.NavigationDestination
import com.example.favoritebooksapp.ui.viewModel.BookDetailsUiState

object BookDetailsDestination : NavigationDestination {
    override val route = "book_details_screen"
    override val titleRes = R.string.all_books
    const val bookIdArg = "bookId"
    val routeWithArgs = "$route/{$bookIdArg}"
}

@Composable
fun BookDetailsScreen(
    bookId: String,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    navigateBack: () -> Unit,
    booksViewModel: BooksViewModel,
    favoritesViewModel: FavoritesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val bookDetailsUiState by booksViewModel.bookDetailsUiState

    LaunchedEffect(bookId) {
        booksViewModel.loadBookDetails(bookId)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.empty_string),
                showBackButton = true,
                onBack =  navigateBack,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (bookDetailsUiState) {
                is BookDetailsUiState.Loading -> LoadingScreen()
                is BookDetailsUiState.Error -> ErrorScreen()
                is BookDetailsUiState.Success -> {
                    val book = (bookDetailsUiState as BookDetailsUiState.Success).book
                    BookDetailsContent(
                        book = book,
                        isFavorite = book.id in favoritesViewModel.favoriteBookIds.collectAsState().value,
                        onToggleFavorite = { favoritesViewModel.toggleFavorite(book.id) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun BookDetailsContent(
    book: BookItem,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val volumeInfo = book.volumeInfo
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://"))
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.loading_img),
            error = painterResource(R.drawable.ic_broken_image),
            contentDescription = volumeInfo.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Text(
            text = volumeInfo.title,
            style = MaterialTheme.typography.headlineSmall
        )

        volumeInfo.authors?.let {
            Text(
                text = "Authors: ${it.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        volumeInfo.publisher?.let {
            Text(
                text = "Publisher: $it",
                style = MaterialTheme.typography.bodySmall
            )
        }

        volumeInfo.publishedDate?.let {
            Text(
                text = "Published: $it",
                style = MaterialTheme.typography.bodySmall
            )
        }

        volumeInfo.categories?.let {
            Text(
                text = "Categories: ${it.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        volumeInfo.description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall
            )
        } ?: Text("No description available.", style = MaterialTheme.typography.bodySmall)

        Icon(
            imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Toggle Favorite",
            modifier = Modifier
                .size(32.dp)
                .clickable { onToggleFavorite() },
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
