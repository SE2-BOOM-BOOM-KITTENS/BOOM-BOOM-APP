package com.example.boomboomfrontend.ui.gameUI

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.boomboomfrontend.viewmodel.LobbyViewModel
import com.example.boomboomfrontend.viewmodel.gameState.GameStateViewModel
import java.util.*

@Composable
fun PlayersInLobbyScreen(
    navController: NavController,
    onEnterGameScreen: () -> Unit,
    lobbyId: String?,
    lobbyViewModel: LobbyViewModel = viewModel(),
    gameStateViewModel: GameStateViewModel = viewModel()
) {
    val players by lobbyViewModel.players.collectAsState()

    LaunchedEffect(lobbyId) {
        lobbyId?.let {
            Log.d("PlayersInLobbyScreen", "Fetching players in lobby: $lobbyId")
            lobbyViewModel.getPlayersInLobby(UUID.fromString(it))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
        ) {
            Text("Players in Lobby $lobbyId", modifier = Modifier.padding(8.dp))
            players.forEach { player ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Black)
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    Text(player.name)
                }
            }
        }

        Button(
            onClick = {
                gameStateViewModel.repository.players = players.toMutableList()
                navController.navigate("game")
                onEnterGameScreen()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("Start Game")
        }

    }
}
