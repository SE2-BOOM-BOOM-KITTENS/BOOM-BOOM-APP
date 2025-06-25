package com.example.boomboomfrontend.ui.gameUI

import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.CardType
import com.example.boomboomfrontend.viewmodel.gameState.GameStateViewModel
import java.util.UUID

fun playCard(viewModel: GameStateViewModel, cardname: String, cardtype: CardType,id:UUID) {
    val cardPlayed = Card(cardname, cardtype,id=id)
    viewModel.playCard(cardPlayed)
}

fun cheat(viewModel: GameStateViewModel,card: Card){
    viewModel.cheat(card)
}

fun passTurn(viewModel: GameStateViewModel){
    viewModel.pass()
}