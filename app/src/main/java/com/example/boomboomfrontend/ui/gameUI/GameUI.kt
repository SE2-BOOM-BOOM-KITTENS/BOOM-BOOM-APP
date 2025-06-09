package com.example.boomboomfrontend.ui.gameUI

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.boomboomfrontend.model.CardType
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
    val cardList = gameStateViewModel.getCardHandText()

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

        Box (
            modifier = Modifier
                .width(1020.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            CardSelect(gameStateViewModel, selectedCardText)
        }

        // Top center
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            OpponentHands()
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
                modifier = Modifier.size(110.dp, 150.dp),
                shape = RectangleShape
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

@Composable
fun CardSelect(gameStateViewModel: GameStateViewModel, selectedCardText: MutableState<String>){
    val labels = gameStateViewModel.getCardHandText()

    val onClicks = gameStateViewModel.cardHand.map { card ->
        {
            selectedCardText.value = card.name
            when (card.type) {
                CardType.BLANK -> bottomLeftBlank(gameStateViewModel)
                CardType.DEFUSE -> bottomLeftDefuse(gameStateViewModel)
                CardType.NOPE -> bottomLeftNope(gameStateViewModel)
                else -> bottomLeftPass(gameStateViewModel) // fallback
            }
        }
    }

        Box (
            contentAlignment = Alignment.CenterEnd
        ) {
            Column(
                modifier = Modifier
                    .background(Color(cardfront))
                    .size(450.dp, 110.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                ButtonGroup(
                    labels = labels,
                    onClicks = onClicks
                )
        }
    }
}