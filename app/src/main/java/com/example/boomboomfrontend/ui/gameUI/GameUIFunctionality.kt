package com.example.boomboomfrontend.ui.gameUI

import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.CardType
import com.example.boomboomfrontend.network.messages.PlayerMessage
import com.example.boomboomfrontend.viewmodel.gameState.GameStateViewModel

fun playCard(viewModel: GameStateViewModel, cardname: String, cardtype: CardType) {
    val player = "PLAYER"
    val action = "PLAY_CARDS"
    val cardsPlayed = Card(cardname, cardtype)
    val cardList: MutableList<Card> = mutableListOf()
    cardList.add(cardsPlayed)

    val message = composeMessage(player, action, cardList)
    viewModel.playCard(message)
}

fun passTurn(viewModel: GameStateViewModel){
    val player = "PLAYER"
    val action = "PASS"

    val message = composeMessage(player,action,null)
    viewModel.pass(message)
}

fun composeMessage(player: String, action: String, cardList: List<Card>?):PlayerMessage{
    return  PlayerMessage(
        player,
        action,
        cardList
    )
}