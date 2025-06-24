package com.example.boomboomfrontend.ui.gameUI

import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.CardType
import com.example.boomboomfrontend.viewmodel.gameState.GameStateViewModel

fun playCard(viewModel: GameStateViewModel, cardname: String, cardtype: CardType) {
    val cardsPlayed = Card(cardname, cardtype)
    val cardList: MutableList<Card> = mutableListOf()
    cardList.add(cardsPlayed)


    viewModel.playCard(cardList)
}

fun passTurn(viewModel: GameStateViewModel){
    viewModel.pass()
}