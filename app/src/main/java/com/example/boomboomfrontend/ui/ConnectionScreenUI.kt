package com.example.boomboomfrontend.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.boomboomfrontend.viewmodel.gameState.ClientInfoHolder


@Composable
fun ConnectionScreen(navController: NavHostController, onEnterGameScreen: () -> Unit) {
    val clientInfo = ClientInfoHolder.clientInfo
    /*
    * Replace all "Player1" mentions with references to the list of players
    * IDK How we'll fetch connection status
    * */

    val players = listOf("PLAYERS", "${clientInfo.playerName}:${clientInfo.playerId}", "Player2", "Player3", "Player4", "Player5")
    val connectionstatus = listOf("CONNECTION STATUS", "HOST", "ONLINE", "CONNECTION PENDING", "OFFLINE", "OFFLINE")

    Column(
        Modifier
            .padding(horizontal = 70.dp, vertical = 70.dp)
            .width(600.dp)) {
        for (i in players.indices) {
            Row(
                Modifier
                    .border(1.dp, Color.Black)
                    .background(if (i == 0) Color.Gray else Color.White)
            ) {
                Text(
                    text = players[i],
                    modifier = Modifier
                        .background(if (i == 0) Color.Gray else Color.LightGray)
                        .weight(1f)
                        .padding(8.dp)
                )
                Text(
                    text = connectionstatus[i],
                    modifier = Modifier
                        .weight(2f)
                        .padding(8.dp),
                )
            }
        }

        Button(
            onClick = {
                navController.navigate("game")
                //gameStateViewModel.startGame()
            }
        ) {
            Text("Spiel starten")
        }
    }
}
