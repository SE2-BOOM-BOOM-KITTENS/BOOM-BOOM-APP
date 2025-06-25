package com.example.boomboomfrontend.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.boomboomfrontend.R
import com.example.boomboomfrontend.ui.gameUI.background
import com.example.boomboomfrontend.viewmodel.PlayerViewModel
import com.example.boomboomfrontend.viewmodel.lobby.LobbyViewModel
import java.util.UUID

@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun ConnectionScreen(
    navController: NavController = rememberNavController(),
    onEnterGameScreen: () -> Unit = { },
    playerViewModel: PlayerViewModel = viewModel(),
    lobbyViewModel: LobbyViewModel = viewModel()
) {
    val clientInfo = lobbyViewModel.clientInfo
    val players by playerViewModel.players.collectAsState()
    val lobbies by lobbyViewModel.lobbies.collectAsState()

    LaunchedEffect(lobbyViewModel.goToLobby) {
        if (lobbyViewModel.goToLobby) {
            navController.navigate("players_in_lobby/${lobbyViewModel.clientInfo.currentLobbyID}")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(background))
    ) {
        Image(
            painter = painterResource(id = R.drawable.gradientbackground),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Row(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
            ) {
                if (lobbies.isNotEmpty()) {
                    Text(
                        "Fetched Lobby IDs:",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(1.dp, Color.Black)
                            .background(Color(0xFFF0F0F0)),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(lobbies.entries.toList()) { entry ->
                            val id = entry.key
                            val lobby = entry.value

                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color.Black)
                                    .background(Color.LightGray)
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Lobby by ${lobby.creator.name}",
                                    modifier = Modifier.weight(1f)
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
                } else {
                    Text("No lobbies found.\nTap \"Fetch Lobbies\" to refresh!")
                }
            }

            Spacer(Modifier.width(24.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                players.forEach { player ->
                    Row(
                        Modifier
                            .border(1.dp, Color.Black)
                            .background(Color.White)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = player.name,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
        
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = {
                lobbyViewModel.getAllLobbies()
                Log.e("LOBBIES", "Request sent to fetch lobbies")
            }) {
                Text("Fetch Lobbies")
            }

            Button(onClick = {
                lobbyViewModel.createLobby(clientInfo.playerId!!)
            }) {
                Text("Create Lobby")
            }
        }
    }
}
