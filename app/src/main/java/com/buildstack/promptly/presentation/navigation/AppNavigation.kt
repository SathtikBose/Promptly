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
import com.buildstack.promptly.presentation.chat.ChatScreen
import com.buildstack.promptly.presentation.chat.ChatViewModel
import com.buildstack.promptly.presentation.chat.ChatViewModelFactory
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
            // For now, jump straight to new chat for testing ChatSystem
            // In Phase 5, this will be the actual Home screen logic
            val factory = ChatViewModelFactory(appContainer.chatRepository, appContainer.groqRepository, 0L)
            val viewModel: ChatViewModel = viewModel(factory = factory)
            ChatScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
