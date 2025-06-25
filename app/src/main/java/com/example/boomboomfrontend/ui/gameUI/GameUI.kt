package com.example.boomboomfrontend.ui.gameUI

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.boomboomfrontend.model.CardType
import com.example.boomboomfrontend.viewmodel.gameState.GameStateViewModel
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.boomboomfrontend.ui.dialogs.ExitPopup
import com.example.boomboomfrontend.R
import com.example.boomboomfrontend.ui.DialogUI
import com.example.boomboomfrontend.ui.dialogs.WinPopup

const val background = 0xff962319
const val cardback = 0xff1c0e0b
const val cheat_cardfront = 0xffac5b4d
const val cardfront = 0xffb2766b
const val border = 0xff000000
const val servertext = 0x99eeeeee

@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun GameScreenPreview(navController: NavController = rememberNavController()){
    GameScreen(navController)
}

@Composable
fun GameScreen(navController: NavController, gameStateViewModel: GameStateViewModel = viewModel()) {
    val selectedCardText = remember { mutableStateOf("") }
    val selectedCardDupe = remember { mutableStateOf(false) }
    val cheatMode = remember { mutableStateOf(false) }
    val serverMessage by gameStateViewModel.responseMessage.collectAsState()

    gameStateViewModel.repository.myTurn = true
    val players = gameStateViewModel.repository.players


    val opponentName1 = players.getOrNull(0)?.name ?: ""
    val opponentName2 = players.getOrNull(1)?.name ?: ""
    val opponentName3 = players.getOrNull(2)?.name ?: ""

    val showCardDialog = remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        showExitDialog = true
    }

    if(showExitDialog){
        ExitPopup(
            onPlay = {

            },
            onPass = {
                gameStateViewModel.exit()
                navController.navigate("connection-screen")
            },
            onDismiss = {
                showExitDialog = false
            }
        )
    }

    if(gameStateViewModel.repository.gameFinished){
        WinPopup(
            onPlay = {
                gameStateViewModel.exit()
                navController.navigate("connection-screen")
            },
            onDismiss = {
                gameStateViewModel.exit()
                navController.navigate("connection-screen")
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(background))
    ) {

        DialogUI(
            visible = showCardDialog.value,
            cards = gameStateViewModel.repository.cardHand,
            onDismiss = { showCardDialog.value = false }
        )

        // Center content
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.backgroundtable),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                CardUI(selectedCardText.value, selectedCardDupe.value)
                DeckUI()
            }
        }

        Box(
            modifier = Modifier
                .width(1020.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            CardSelect(gameStateViewModel, selectedCardText, selectedCardDupe,cheatMode)
        }

        Box (
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            PassButton(gameStateViewModel)
        }

        Row (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            ExitButton(gameStateViewModel)
            CheatButton(gameStateViewModel, cheatMode)
            CheckCheatButton(gameStateViewModel)
        }

        // Left
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            LeftOpponentHand(opponentName2)
        }

        // Top center
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            TopOpponentHand(opponentName1)
        }


        //Right
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterEnd
        ) {
            RightOpponentHand(opponentName3)
        }

        // Top Right
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            ServerMessage(serverMessage)
        }
    }
}

@Composable
fun PassButton(gameStateViewModel: GameStateViewModel) {
    Button(
        enabled = gameStateViewModel.repository.myTurn && !gameStateViewModel.lockButtons,
        onClick = { passTurn(gameStateViewModel) },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(cardback)
        ),
        modifier = Modifier.size(120.dp, 40.dp)
            .border(2.dp, Color(border), RoundedCornerShape(10.dp))
            .background(Color(cardfront), RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = "Pass Turn",
            color = Color.White,
            fontSize = 13.sp)
    }
}

@Composable
fun CheatButton(gameStateViewModel: GameStateViewModel, cheatMode:MutableState<Boolean>){
    Button(
        enabled = gameStateViewModel.repository.myTurn && !gameStateViewModel.lockButtons,
        onClick = {
            cheatMode.value = !cheatMode.value
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(cardback)
        ),
        modifier = Modifier.size(120.dp, 40.dp)
            .border(2.dp, Color(border), RoundedCornerShape(10.dp))
            .background(Color(cardfront), RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp)
    ) {
        val label = if (cheatMode.value) "Cheat: ON" else "Cheat: OFF"
        Text(text = label,
            color = Color.White,
            fontSize = 13.sp)
    }
}
@Composable
fun CheckCheatButton(gameStateViewModel: GameStateViewModel){
    Button(
        onClick = {
          gameStateViewModel.checkCheat()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(cardback)
        ),
        modifier = Modifier.size(120.dp, 40.dp)
            .border(2.dp, Color(border), RoundedCornerShape(10.dp))
            .background(Color(cardfront), RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = "Check Cheat",
            color = Color.White,
            fontSize = 13.sp)
    }
}

@Composable
fun ExitButton(gameStateViewModel: GameStateViewModel) {
    Button(
        enabled = gameStateViewModel.repository.myTurn && !gameStateViewModel.lockButtons,
        onClick = { },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(cardback)
        ),
        modifier = Modifier.size(120.dp, 40.dp)
            .border(2.dp, Color(border), RoundedCornerShape(10.dp))
            .background(Color(cardback), RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = "Exit Game",
            color = Color.White,
            fontSize = 13.sp)
    }
}

@Composable
fun CardUI(textField: String, isDupe:Boolean) {
    val frontColor = if (isDupe) Color(cheat_cardfront) else Color(cardfront)
    Box(
        modifier = Modifier
            .size(110.dp, 150.dp)
            .background(frontColor, RoundedCornerShape(10.dp))
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
fun TopOpponentHand(opponentName: String) {
    Box(
        modifier = Modifier
            .size(250.dp, 90.dp)
            .background(Color(cardback))
    ) {
        Text(
            text = opponentName,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun LeftOpponentHand(opponentName: String) {
    Box(
        modifier = Modifier
            .size(250.dp, 90.dp)
            .offset((-80).dp)
            .vertical()
            .rotate(-90f)
            .background(Color(cardback))
    ) {
        Text(
            text = opponentName,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxHeight()
                .padding(30.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun RightOpponentHand(opponentName: String) {
    Box(
        modifier = Modifier
            .size(250.dp, 90.dp)
            .offset((80).dp)
            .vertical()
            .rotate(90f)
            .background(Color(cardback))
    ) {
        Text(
            text = opponentName,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxHeight()
                .padding(30.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

fun Modifier.vertical() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }

@Composable
fun Cards(labels: List<String>, onClicks: List<() -> Unit>, gameStateViewModel: GameStateViewModel, dupes: List<Boolean>) {
    Row(
        modifier = Modifier.background(Color(cardback)),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        labels.zip(onClicks).zip(dupes).forEach { (pair, isDupe) ->
            val frontColor = if (isDupe) Color(cheat_cardfront) else Color(cardfront)
            Button(
                enabled = gameStateViewModel.repository.myTurn && !gameStateViewModel.lockButtons,
                onClick = pair.second,
                colors = ButtonDefaults.buttonColors(
                    containerColor = frontColor
                ),
                modifier = Modifier.size(90.dp, 150.dp)
                    .border(2.dp, Color(border), RoundedCornerShape(10.dp))
                    .background(Color(cardfront), RoundedCornerShape(10.dp)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = pair.first,
                    color = Color.White,
                    fontSize = 13.sp)
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
fun CardSelect(
    gameStateViewModel: GameStateViewModel,
    selectedCardText: MutableState<String>,
    selectedCardCheated: MutableState<Boolean>,
    cheatMode: MutableState<Boolean>
){
    val labels = gameStateViewModel.repository.getCardHandText()
    val dupes = gameStateViewModel.repository.getCardCheatValue()

    val onClicks = gameStateViewModel.repository.cardHand.map { card ->
        {
            selectedCardText.value = card.name
            selectedCardCheated.value = card.cheatDuplicated
            if(cheatMode.value){
                cheat(gameStateViewModel,card)
            }
            else {
                when (card.type) {
                    CardType.BLANK -> playCard(gameStateViewModel, "Blank", CardType.BLANK, card.id!!)
                    CardType.DEFUSE -> playCard(gameStateViewModel, "Defuse", CardType.DEFUSE,card.id!!)
                    CardType.NOPE -> playCard(gameStateViewModel, "Nope", CardType.NOPE, card.id!!)
                    CardType.SHUFFLE -> playCard(gameStateViewModel, "Shuffle", CardType.SHUFFLE, card.id!!)
                    CardType.SEE_THE_FUTURE -> playCard(
                        gameStateViewModel,
                        "See the Future",
                        CardType.SEE_THE_FUTURE,
                        card.id!!
                    )

                    CardType.ALTER_THE_FUTURE -> playCard(
                        gameStateViewModel,
                        "Alter the Future",
                        CardType.ALTER_THE_FUTURE,
                        card.id!!
                    )

                    CardType.REVERSE -> playCard(gameStateViewModel, "Reverse", CardType.REVERSE, card.id!!)
                    CardType.DRAW_FROM_THE_BOTTOM -> playCard(
                        gameStateViewModel,
                        "Draw from the Bottom",
                        CardType.DRAW_FROM_THE_BOTTOM,
                        card.id!!
                    )

                    CardType.ATTACK -> playCard(gameStateViewModel, "Attack", CardType.ATTACK, card.id!!)
                    CardType.SKIP -> playCard(gameStateViewModel, "Skip", CardType.SKIP, card.id!!)
                    CardType.CAT_BEARD -> playCard(
                        gameStateViewModel,
                        "Cat beard",
                        CardType.CAT_BEARD,
                        card.id!!
                    )

                    else -> passTurn(gameStateViewModel) // fallback
                }
            }
        }
    }

        Box (
            contentAlignment = Alignment.CenterEnd
        ) {
            Column(
                modifier = Modifier
                    .background(Color(cardback))
                    .size(450.dp, 110.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                Cards(
                    labels = labels,
                    onClicks = onClicks,
                    gameStateViewModel = gameStateViewModel,
                    dupes = dupes
                )
        }
    }
}