package com.example.boomboomfrontend.ui.gameUI

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.boomboomfrontend.R
import com.example.boomboomfrontend.ui.dialogs.ExitPopup
import com.example.boomboomfrontend.viewmodel.lobby.LobbyViewModel
import java.util.*

//@Preview(
//    showSystemUi = true,
//    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
//)
@Composable
fun PlayersInLobbyScreen(
    navController: NavController = rememberNavController(),
    onEnterGameScreen: () -> Unit = { },
    lobbyId: String?,
    lobbyViewModel: LobbyViewModel = viewModel()
) {
    val players by lobbyViewModel.players.collectAsState()

    LaunchedEffect(Unit) {
        lobbyViewModel.connect()
    }

    LaunchedEffect(lobbyViewModel.goToGame) {
        if (lobbyViewModel.goToGame) {
            navController.navigate("game")
        }
    }

    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        showExitDialog = true
    }

    if(showExitDialog){
        ExitPopup(
            onPlay = {

            },
            onPass = {
                val info = lobbyViewModel.clientInfo
                lobbyViewModel.leaveLobby(info.currentLobbyID.toString(),info.playerId)
                info.currentLobbyID = null
                lobbyViewModel.goToLobby = false
                navController.navigate("connection-screen")
            },
            onDismiss = {
                showExitDialog = false
            }
        )
    }


    LaunchedEffect(lobbyId) {
        lobbyId?.let {
            Log.d("PlayersInLobbyScreen", "Fetching players in lobby: $lobbyId")
            lobbyViewModel.getPlayersInLobby(UUID.fromString(it))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.gradientbackground),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

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

        Box(
            modifier = Modifier.fillMaxSize().padding(30.dp),
            contentAlignment = Alignment.BottomEnd
        ){
            Button(
                onClick = {
                    lobbyViewModel.createGame()
                    Log.i("Debug","Request sent")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(cardback)
                ),
                modifier = Modifier.size(180.dp, 60.dp)
                    .border(2.dp, Color(border), RoundedCornerShape(10.dp))
                    .background(Color(cardback), RoundedCornerShape(10.dp)),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(text = "Start Game",
                fontSize = 18.sp)
            }
        }
    }
}
