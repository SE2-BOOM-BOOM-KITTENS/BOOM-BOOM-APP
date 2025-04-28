package com.example.boomboomfrontend.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.boomboomfrontend.logic.Lobby
import com.example.boomboomfrontend.model.ConnectionStatus
import com.example.boomboomfrontend.model.Player
import com.example.boomboomfrontend.ui.theme.BoomBoomFrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoomBoomFrontendTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "lobby") {
                    composable("lobby") { LobbyScreen(navController) }
                    composable("test-server") { ServerTestActivity() }
                }
            }
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
}

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

@Composable
fun ArrangementUI() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CardUI()
            DeckUI()
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        PlayerHands()
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        OpponentHands()
    }
}

@Composable
fun CardUI() {
    Box(
        modifier = Modifier
            .size(110.dp, 150.dp)
            .background(Color(0xffb2766b))
    ) {
        Text(
            text = "BLANK\nCARD",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun DeckUI() {
    Box(
        modifier = Modifier
            .size(110.dp, 150.dp)
            .background(Color(0xff1c0e0b))
    ) {
        Text(
            text = "DECK",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun PlayerHands() {
    Box(
        modifier = Modifier
            .size(250.dp, 90.dp)
            .background(Color(0xffb2766b))
    ) {
        Text(
            text = "PLAYER\nHAND",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun OpponentHands() {
    Box(
        modifier = Modifier
            .size(250.dp, 90.dp)
            .background(Color(0xff1c0e0b))
    ) {
        Text(
            text = "OPPONENT\nHAND",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true, device = "spec:orientation=landscape,width=411dp,height=891dp")
@Composable
fun GreetingPreview() {
    BoomBoomFrontendTheme {
        ArrangementUI()
    }
}
