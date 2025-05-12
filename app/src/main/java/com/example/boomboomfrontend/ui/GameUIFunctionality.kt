package com.example.boomboomfrontend.ui

import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.CardType
import com.example.boomboomfrontend.network.messages.PlayerMessage

fun bottomLeftBlank() {
    val player = "PLAYER"
    val action = "CARDS_PLAYED"
    val cardsPlayed = Card("Blank", CardType.BLANK)
    val cardList = mutableListOf(cardsPlayed)

    ComposeMessage(player, action, cardList)
}

fun bottomLeftDefuse() {
    val player = "PLAYER"
    val action = "CARDS_PLAYED"
    val cardsPlayed = Card("Defuse", CardType.DEFUSE)
    val cardList = mutableListOf(cardsPlayed)

    ComposeMessage(player, action, cardList)
}

fun bottomLeftNope() {
    val player = "PLAYER"
    val action = "CARDS_PLAYED"
    val cardsPlayed = Card("Nope", CardType.NOPE)
    val cardList = mutableListOf(cardsPlayed)

    ComposeMessage(player, action, cardList)
}

fun ComposeMessage(player: String, action: String, cardList: List<Card>){
    val message = PlayerMessage(
        player,
        action,
        cardList
    )
}