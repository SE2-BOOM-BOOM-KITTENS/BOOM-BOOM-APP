package com.example.boomboomfrontend.ui.gameUI

import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.CardType
import com.example.boomboomfrontend.viewmodel.gameState.GameStateViewModel

fun playCard(viewModel: GameStateViewModel, cardname: String, cardtype: CardType) {
    val cardPlayed = Card(cardname, cardtype)
    viewModel.playCard(cardPlayed)
}

fun passTurn(viewModel: GameStateViewModel){
    viewModel.pass()
}