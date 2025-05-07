package com.example.boomboomfrontend.ui.theme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.boomboomfrontend.model.ConnectionStatus
import kotlinx.coroutines.*
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyUI() {
    var playerName by remember { mutableStateOf("") }
    var lobbyCode by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(ConnectionStatus.NOT_CONNECTED) }
    var countdown by remember { mutableStateOf<Int?>(null) }

    Column{
        Text("Name:")
        TextField(value = playerName, onValueChange = { playerName = it })

        Spacer(androidx.compose.ui.Modifier.height(8.dp))

        Button(onClick = {
            status = ConnectionStatus.JOINED
            lobbyCode = Random.nextInt(1000, 9999).toString()
        }) {
            Text("Lobby erstellen")
        }

        Button(onClick = {
            status = ConnectionStatus.CONNECTION_PENDING
            CoroutineScope(Dispatchers.Default).launch {
                delay(Random.nextLong(1000, 3000))
                status = ConnectionStatus.JOINED
                countdown = 5
                for (i in 5 downTo 1) {
                    countdown = i
                    delay(1000)
                }
                // Spielstart: Logik oder Navigation hier einfügen
            }
        }) {
            Text("Lobby beitreten")
        }

        Spacer(androidx.compose.ui.Modifier.height(16.dp))
        Text("Status: $status")

        countdown?.let {
            Text("Spiel startet in $it Sekunden...")
        }
    }
}