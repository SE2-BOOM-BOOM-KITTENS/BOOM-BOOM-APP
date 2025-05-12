package com.example.boomboomfrontend.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.boomboomfrontend.viewmodel.GameStateViewModel

@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun GameScreen(gameStateViewModel: GameStateViewModel = viewModel()) {
    val selectedCardText = remember { mutableStateOf("BLANK\nCARD") }

    Text(text = gameStateViewModel.playerName)

    Box(modifier = Modifier.fillMaxSize()) {
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
            PlayerHands()
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
                labels = listOf("Blank", "Defuse", "Nope"),
                onClicks = listOf(
                    {
                        selectedCardText.value = "BLANK\nCARD"
                        bottomLeftBlank()
                    },
                    {
                        selectedCardText.value = "DEFUSE"
                        bottomLeftDefuse()
                    },
                    {
                        selectedCardText.value = "NOPE"
                        bottomLeftNope()
                    }
                )
            )
        }
    }
}




@Composable
fun CardUI(textField: String) {
    Box(
        modifier = Modifier
            .size(110.dp, 150.dp)
            .background(Color(0xffb2766b))
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

@Composable
fun ButtonGroup(
    labels: List<String>,
    onClicks: List<() -> Unit>
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
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
