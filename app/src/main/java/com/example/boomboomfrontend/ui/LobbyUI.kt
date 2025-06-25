package com.example.boomboomfrontend.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.boomboomfrontend.model.LobbyPlayer
import com.example.boomboomfrontend.viewmodel.lobby.LobbyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen(lobbyViewModel: LobbyViewModel = viewModel(), navController: NavHostController, onEnterLobby: () -> Unit) {
    var name by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Dein Name:")
        TextField(value = name, onValueChange = { name = it })

        Spacer(Modifier.height(8.dp))

        Button(onClick = {
   lobbyViewModel.createLobby(LobbyPlayer(name), 4)
        }) {
            Text("Lobby erstellen")
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = {
            navController.navigate("connection-screen")
        }) {
            Text("Lobby beitreten")
        }
    }
}

