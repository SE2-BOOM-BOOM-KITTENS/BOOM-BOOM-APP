package com.example.boomboomfrontend.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.boomboomfrontend.viewmodel.lobby.LobbyViewModel
import com.example.boomboomfrontend.viewmodel.PlayerViewModel
import com.example.boomboomfrontend.viewmodel.gameState.ClientInfoHolder
import java.util.UUID

@Composable
fun ConnectionScreen(
    navController: NavController,
    onEnterGameScreen: () -> Unit,
    playerViewModel: PlayerViewModel = viewModel(),
    lobbyViewModel: LobbyViewModel = viewModel()
) {
    val clientInfo = ClientInfoHolder.clientInfo
    val players by playerViewModel.players.collectAsState()
    val lobbies by lobbyViewModel.lobbies.collectAsState()

    Column(
        Modifier
            .padding(horizontal = 70.dp, vertical = 70.dp)
            .width(600.dp)
    ) {
        // Fetch lobbies button
        Button(onClick = {
            lobbyViewModel.getAllLobbies()
            Log.e("LOBBIES", "Request sent to fetch lobbies")
        }) {
            Text("Fetch Lobbies")
        }

        // Display all lobbies
        if (lobbies.isNotEmpty()) {
            Text("Fetched Lobby IDs:", modifier = Modifier.padding(vertical = 8.dp))
            lobbies.forEach { (id, lobby) ->
                Row(
                    Modifier
                        .border(1.dp, Color.Black)
                        .background(Color.LightGray)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Lobby by ${lobby.creator.name} (ID: $id)",
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )

                    Button(
                        onClick = {
                            lobbyViewModel.joinLobby(id, clientInfo.playerId)
                            clientInfo.currentLobbyID = UUID.fromString(id)
                            Log.e("ConnectionScreen", "Joining lobby: $id")
                            navController.navigate("players_in_lobby/$id")
                        }
                    ) {
                        Text("Join")
                    }
                }
            }
        }

        // Show current players
        players.forEachIndexed { i, player ->
            Row(
                Modifier
                    .border(1.dp, Color.Black)
                    .background(Color.White)
            ) {
                Text(
                    text = player.name,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                )
                Text(
                    text = "ONLINE",
                    modifier = Modifier
                        .weight(2f)
                        .padding(8.dp)
                )
            }
        }
    }
}
