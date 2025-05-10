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
import androidx.navigation.NavHostController
import com.example.boomboomfrontend.logic.Lobby
import com.example.boomboomfrontend.model.ConnectionStatus
import com.example.boomboomfrontend.model.Player
import kotlinx.coroutines.*
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var lobby: Lobby? by remember { mutableStateOf(null) }
    var joined by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Dein Name:")
        TextField(value = name, onValueChange = { name = it })

        Spacer(Modifier.height(8.dp))

        Button(onClick = {
            if (name.isNotBlank()) {
                val host = Player(id = "0", name, ConnectionStatus.JOINED, 1, isAlive = true)
                lobby = Lobby(host, maxPlayers = 4)
            }
        }) {
            Text("Lobby erstellen")
        }

        Spacer(Modifier.height(8.dp))

        lobby?.let {
            Text("Lobby-Code: ${it.getRoomCode()}")
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = {
            val p = Player(id = "1", name = "Player", ConnectionStatus.NOT_CONNECTED, 1, isAlive = true)
            if (lobby?.joinLobby(p) == true) {
                joined = true
            }
        }) {
            Text("Lobby beitreten")
        }

        Spacer(Modifier.height(16.dp))
        Text("Status: ${if (joined) "Beigetreten" else "Nicht verbunden"}")

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("test-server")
        }) {
            Text("Zum Server/Client Test")
        }
    }
}
