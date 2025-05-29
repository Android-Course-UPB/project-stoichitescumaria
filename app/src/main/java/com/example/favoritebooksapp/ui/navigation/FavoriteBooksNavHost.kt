package com.example.favoritebooksapp.ui.navigation

import BookDetailsScreen
import android.R.attr.type
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.favoritebooksapp.AppViewModelProvider
import com.example.favoritebooksapp.ui.screens.AllBooksDestination
import com.example.favoritebooksapp.ui.screens.AllBooksScreen
import com.example.favoritebooksapp.ui.screens.FavoriteBooksDestination
import com.example.favoritebooksapp.ui.screens.FavoriteBooksScreen
import com.example.favoritebooksapp.ui.screens.SearchBooksDestination
import com.example.favoritebooksapp.ui.screens.SearchBooksScreen
import com.example.favoritebooksapp.ui.screens.WelcomeDestination
import com.example.favoritebooksapp.ui.screens.WelcomeScreen
import com.example.favoritebooksapp.ui.viewModel.BooksViewModel
import com.example.favoritebooksapp.ui.viewModel.FavoritesViewModel

@Composable
fun FavoriteBooksNavHost (
    navController: NavHostController,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {

    val booksViewModel: BooksViewModel = viewModel(factory = AppViewModelProvider.Factory)

    val favoriteBooksModel: FavoritesViewModel = viewModel(factory = AppViewModelProvider.Factory)


    NavHost(
        navController = navController,
        startDestination = WelcomeDestination.route,
        modifier = Modifier
    ) {
        composable(route = WelcomeDestination.route) {
            WelcomeScreen(
                isDarkTheme,
                onToggleTheme,
                navigateToSearch = { navController.navigate(SearchBooksDestination.route) },
                navigateToFavoriteBooks = {
                    navController.navigate(FavoriteBooksDestination.route)
                }
            )
        }
        composable(route = SearchBooksDestination.route) {
            SearchBooksScreen(
                isDarkTheme,
                onToggleTheme,
                navigateBack = { navController.popBackStack() },
                navigateToLibrary  = {
                    navController.navigate(AllBooksDestination.route)
                },
                booksViewModel = booksViewModel
            )
        }
        composable(route = AllBooksDestination.route) {
            AllBooksScreen(
                isDarkTheme,
                onToggleTheme,
                navigateBack = { navController.popBackStack() },
                booksViewModel = booksViewModel,
                navigateToDetails  = { id ->
                    navController.navigate("${BookDetailsDestination.route}/${id}")
                },
                favoritesViewModel = favoriteBooksModel
            )
        }
        composable(route = FavoriteBooksDestination.route) {
            FavoriteBooksScreen(
                isDarkTheme,
                onToggleTheme,
                navigateBack = { navController.popBackStack() },
                navigateToDetails  = { id ->
                    navController.navigate("${BookDetailsDestination.route}/${id}")
                },
                favoriteBooksModel
            )
        }
        composable(
            route = BookDetailsDestination.routeWithArgs,
            arguments = listOf(
                navArgument(BookDetailsDestination.bookIdArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString(BookDetailsDestination.bookIdArg) ?: return@composable
            BookDetailsScreen(
                bookId = bookId,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                navigateBack = { navController.popBackStack() },
                booksViewModel = booksViewModel,
            )
        }

    }
}