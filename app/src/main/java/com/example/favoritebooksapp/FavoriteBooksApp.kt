package com.example.favoritebooksapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.favoritebooksapp.ui.navigation.FavoriteBooksNavHost

@Composable
fun FavoriteBooksApp (
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    FavoriteBooksNavHost(navController = rememberNavController(), isDarkTheme, onToggleTheme)
}
