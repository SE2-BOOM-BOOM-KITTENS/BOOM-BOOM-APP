package com.example.boomboomfrontend.network.messages

import com.example.boomboomfrontend.model.Card

data class PlayerMessage (
    val playerName: String,
    val action: String,
    val cardsPlayed: List<Card>){

}