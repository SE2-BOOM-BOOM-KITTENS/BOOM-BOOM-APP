package com.example.boomboomfrontend.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.boomboomfrontend.ui.ConnectionScreen
import com.example.boomboomfrontend.ui.gameUI.GameScreen
import com.example.boomboomfrontend.ui.RegisterScreen
import com.example.boomboomfrontend.ui.gameUI.PlayersInLobbyScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoomBoomKittens()
        }
    }

    @Composable
    fun BoomBoomKittens() {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "register") {
                composable("register"){
                    RegisterScreen(navController)
                }
                composable("connection-screen") {
                    ConnectionScreen(navController = navController, onEnterGameScreen = {
                        navController.navigate("game")
                    })
                }
                composable("game") {
                    GameScreen(navController= navController)
                }

                composable("players_in_lobby/{lobbyId}") { backStackEntry ->
                    val lobbyId = backStackEntry.arguments?.getString("lobbyId")
                    PlayersInLobbyScreen(lobbyId = lobbyId.toString(), navController = navController, onEnterGameScreen = {
                        navController.navigate("game")
                    })
                }
            }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
}