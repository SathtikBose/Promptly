package com.buildstack.promptly.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.buildstack.promptly.di.AppContainer
import com.buildstack.promptly.presentation.home.HomeScreen
import com.buildstack.promptly.presentation.settings.SettingsScreen
import com.buildstack.promptly.presentation.settings.SettingsViewModel
import com.buildstack.promptly.presentation.settings.SettingsViewModelFactory
import com.buildstack.promptly.presentation.splash.SplashScreen

@Composable
fun AppNavigation(appContainer: AppContainer) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                appContainer = appContainer,
                onNavigateToSettings = { navController.navigate("settings") }
            )
        }
        composable("settings") {
            val factory = SettingsViewModelFactory(appContainer.settingsRepository, appContainer.chatRepository)
            val viewModel: SettingsViewModel = viewModel(factory = factory)
            SettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
