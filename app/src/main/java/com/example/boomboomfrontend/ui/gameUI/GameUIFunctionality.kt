package com.example.boomboomfrontend.ui.gameUI

import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.CardType
import com.example.boomboomfrontend.network.messages.PlayerMessage
import com.example.boomboomfrontend.viewmodel.GameStateViewModel

fun bottomLeftBlank(viewModel:GameStateViewModel) {
    val player = "PLAYER"
    val action = "PLAY_CARDS"
    val cardsPlayed = Card("Blank", CardType.BLANK)
    val cardList :MutableList<Card> = mutableListOf()
    cardList.add(cardsPlayed)

    val message = composeMessage(player, action, cardList)
    viewModel.playCard(message)
}

fun bottomLeftDefuse(viewModel: GameStateViewModel) {
    val player = "PLAYER"
    val action = "PLAY_CARDS"
    val cardsPlayed = Card("Defuse", CardType.DEFUSE)
    val cardList :MutableList<Card> = mutableListOf()
    cardList.add(cardsPlayed)

    val message = composeMessage(player, action, cardList)
    viewModel.playCard(message)
}

fun bottomLeftNope(viewModel: GameStateViewModel) {
    val player = "PLAYER"
    val action = "PLAY_CARDS"
    val cardsPlayed = Card("Nope", CardType.NOPE)
    val cardList :MutableList<Card> = mutableListOf()
    cardList.add(cardsPlayed)

    val message = composeMessage(player, action, cardList)
    viewModel.playCard(message)
}

fun bottomLeftPass(viewModel: GameStateViewModel){
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