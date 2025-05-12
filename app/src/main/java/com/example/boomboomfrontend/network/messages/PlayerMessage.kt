package com.example.boomboomfrontend.network.messages

data class PlayerMessage (
    val playerName: String,
    val action: String,
    val cardsPlayed: List<CardNetworkPacket> ){

}