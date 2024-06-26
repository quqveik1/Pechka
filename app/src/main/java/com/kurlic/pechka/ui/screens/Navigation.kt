package com.kurlic.pechka.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HeatModeSelectScreenName
    ) {
        composable(HeatModeSelectScreenName) {
            HeatModeSelectScreen(navController = navController)
        }
    }
}