package com.example.boomboomfrontend.activities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.boomboomfrontend.viewmodel.PlayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerTestActivity(viewModel: PlayerViewModel = viewModel()) {
    var inputText by remember { mutableStateOf("") }
    val playerList by viewModel.players.collectAsState()
    val serverMessage by viewModel.responseMessage.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Linke Seite: Eingabe
            Column(modifier = Modifier.weight(1f)) {
                Text("Eingabe:")

                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.addPlayer(inputText)
                        inputText = ""
                    }
                }) {
                    Text("Add Player")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    viewModel.getAllPlayers()
                }) {
                    Text("Show all Players")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    viewModel.connectWebSocket(inputText)
                }) {
                    Text("Connect WebSocket")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    viewModel.sendAction()
                }) {
                    Text("Send Hello Message")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    viewModel.sendJsonMessage(inputText)
                    inputText = ""
                }) {
                    Text("Send JSON Message")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    viewModel.disconnectWebSocket()
                }) {
                    Text("Disconnect WebSocket")
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Rechte Seite: Ausgabe
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                Text("Ausgabe:")
                Spacer(modifier = Modifier.height(4.dp))

                if (serverMessage.isNotBlank()) {
                    Text(serverMessage, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                playerList.forEach {
                    Text(it.name)
                }
            }
        }
    }
}
