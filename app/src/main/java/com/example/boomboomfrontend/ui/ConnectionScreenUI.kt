package com.example.boomboomfrontend.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.boomboomfrontend.viewmodel.LobbyViewModel
import com.example.boomboomfrontend.viewmodel.PlayerViewModel
import com.example.boomboomfrontend.viewmodel.gameState.ClientInfoHolder


@Composable
fun ConnectionScreen(
    navController: NavHostController,
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
        Button(onClick = {
            lobbyViewModel.getAllLobbies()
            Log.e("LOBBIES", "Request sent to fetch lobbies")
        }) {
            Text("Fetch Lobbies")
        }

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
                            Log.e("ConnectionScreen", "Joining lobby: $id")
                        }
                    ) {
                        Text("Join")
                    }
                }
            }
        }

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

        Button(
            onClick = {
                navController.navigate("game")
                onEnterGameScreen()
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Spiel starten")
        }
    }
}
