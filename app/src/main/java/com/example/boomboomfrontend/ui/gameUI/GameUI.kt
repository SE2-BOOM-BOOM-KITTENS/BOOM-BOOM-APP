package com.example.boomboomfrontend.ui.gameUI

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.boomboomfrontend.viewmodel.GameStateViewModel

const val background = 0xff962319
const val cardback = 0xff1c0e0b
const val cardfront = 0xffb2766b
const val border = 0xff000000
const val servertext = 0x99eeeeee

@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun GameScreen(gameStateViewModel: GameStateViewModel = viewModel()) {
    val selectedCardText = remember { mutableStateOf("BLANK\nCARD") }

    val serverMessage by gameStateViewModel.responseMessage.collectAsState()

    Text(text = gameStateViewModel.playerName)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(background))
    ) {
        // Center content
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                CardUI(selectedCardText.value)
                DeckUI()
            }
        }

        // Bottom center
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            PlayerHands(viewModel())
        }

        // Top center
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            OpponentHands()
        }

        // Bottom left
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            ButtonGroup(
                labels = listOf("Blank", "Defuse", "Nope", "Pass"),
                onClicks = listOf(
                    {
                        selectedCardText.value = "BLANK\nCARD"
                        bottomLeftBlank(gameStateViewModel)
                    },
                    {
                        selectedCardText.value = "DEFUSE"
                        bottomLeftDefuse(gameStateViewModel)
                    },
                    {
                        selectedCardText.value = "NOPE"
                        bottomLeftNope(gameStateViewModel)
                    },
                    {
                        bottomLeftPass(gameStateViewModel)
                    }
                )
            )
        }

        // Top Right
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.TopEnd
        ){
            ServerMessage(serverMessage)
        }
    }
}

@Composable
fun CardUI(textField: String) {
    Box(
        modifier = Modifier
            .size(110.dp, 150.dp)
            .background(Color(cardfront), RoundedCornerShape(10.dp))
            .border(2.dp, Color(border), RoundedCornerShape(10.dp))
    ) {
        Text(
            text = textField,
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
            .background(Color(cardback), RoundedCornerShape(10.dp))
            .border(2.dp, Color(border), RoundedCornerShape(10.dp))
    ) {
        Text(
            text = "DECK",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun PlayerHands(viewModel: GameStateViewModel) {
    Box(
        modifier = Modifier
            .size(250.dp, 90.dp)
            .background(Color(cardfront))
    ) {
        Text(
            text = viewModel.getCardHandText(),
            color = Color.White,
            fontSize = 11.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun OpponentHands() {
    Box(
        modifier = Modifier
            .size(250.dp, 90.dp)
            .background(Color(cardback))
    ) {
        Text(
            text = "OPPONENT\nHAND",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ButtonGroup(
    labels: List<String>,
    onClicks: List<() -> Unit>
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        labels.zip(onClicks).forEach { (label, onClick) ->
            Button(
                onClick = onClick,
                modifier = Modifier.size(width = 95.dp, height = 36.dp) // adjust as needed
            ) {
                Text(text = label)
            }
        }
    }
}

@Composable
fun ServerMessage(serverMessage: String){
    Row(
        modifier = Modifier
            .background(Color(servertext))
            .padding(8.dp)
            .width(240.dp)
    ) {
        Text("Ausgabe:")
        Spacer(modifier = Modifier.height(4.dp))

        if (serverMessage.isNotBlank()) {
            Text(serverMessage, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
