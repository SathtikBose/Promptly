package com.buildstack.promptly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.buildstack.promptly.presentation.navigation.AppNavigation
import com.buildstack.promptly.theme.PromptlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appContainer = (application as PromptlyApplication).container
        setContent {
            PromptlyTheme {
                AppNavigation(appContainer)
            }
        }
    }
}
