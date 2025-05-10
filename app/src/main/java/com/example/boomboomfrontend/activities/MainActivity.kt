package com.example.boomboomfrontend.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.boomboomfrontend.ui.GameScreen
import com.example.boomboomfrontend.ui.defaults.BoomBoomFrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoomBoomFrontendTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "lobby") {
//                    composable("lobby") { LobbyScreen(navController) }
                    composable("lobby") { GameScreen() }
                    composable("test-server") { ServerTestActivity() }
                }
            }
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
}