package com.example.favoritebooksapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.favoritebooksapp.R
import com.example.favoritebooksapp.ui.components.TopBar
import com.example.favoritebooksapp.ui.navigation.NavigationDestination
import com.example.favoritebooksapp.ui.viewModel.BooksUiState
import com.example.favoritebooksapp.ui.viewModel.BooksViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object SearchBooksDestination : NavigationDestination {
    override val route = "search_library"
    override val titleRes = R.string.app_name
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBooksScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    navigateBack: () -> Unit = {},
    navigateToLibrary: () -> Unit,
    booksViewModel: BooksViewModel
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }


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
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(R.string.search_title)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text(stringResource(R.string.search_author)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.name())
                    val encodedAuthor = URLEncoder.encode(author, StandardCharsets.UTF_8.name())
                    booksViewModel.setSearchQuery(encodedTitle, encodedAuthor)
                    navigateToLibrary()
                          },
                enabled = title.isNotBlank()
            ) {
                Text(stringResource(R.string.search_text))
            }
        }
    }
}
