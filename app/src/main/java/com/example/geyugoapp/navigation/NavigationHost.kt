package com.example.geyugoapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geyugoapp.feature.categories.CategoriesScreen
import com.example.geyugoapp.feature.firstuser.FirstUserScreen
import com.example.geyugoapp.feature.main.MainScreen
import com.example.geyugoapp.navigation.categories.CategoriesRoute
import com.example.geyugoapp.navigation.firstuser.FirstUserRoute
import com.example.geyugoapp.navigation.main.MainRoute

@Composable
fun NavigationHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = FirstUserRoute
    ) {
        composable<FirstUserRoute> {
            FirstUserScreen(
                navController = navController
            )
        }
        composable<MainRoute> {
            MainScreen(
                navController = navController
            )
        }
        composable<CategoriesRoute> {
            CategoriesScreen()
        }
    }
}