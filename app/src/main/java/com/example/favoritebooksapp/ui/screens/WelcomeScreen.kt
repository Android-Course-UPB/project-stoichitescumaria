package com.example.favoritebooksapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.favoritebooksapp.R
import com.example.favoritebooksapp.ui.components.TopBar
import com.example.favoritebooksapp.ui.navigation.NavigationDestination

object WelcomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onBackPressed: () -> Unit = {},
    navigateToSearch: () -> Unit,
    navigateToFavoriteBooks: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.empty_string),
                showBackButton = true,
                isDarkTheme = isDarkTheme,
                onBack = onBackPressed,
                onToggleTheme = onToggleTheme
            )
        }
    ) { innerPadding ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.welcome),
                    fontSize = 32.sp,
                    modifier = Modifier.padding(bottom = 32.dp),
                    textAlign = TextAlign.Center,
                    lineHeight = 34.sp
                )

                Button(
                    onClick = navigateToSearch,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.browse_library),
                        fontSize = 25.sp,
                    )
                }

                OutlinedButton(
                    onClick = navigateToFavoriteBooks,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.view_favorite_books),
                        fontSize = 25.sp
                    )
                }
            }
        }
    }
}
