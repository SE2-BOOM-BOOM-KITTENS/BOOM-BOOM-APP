package com.example.boomboomfrontend.logic

import com.example.boomboomfrontend.model.Player

class GameManager(
    private val cardManager: CardManager,
    private val players: MutableList<Player>
) {

    // fixme dont forget to send updates to the server

    var currentPlayer: Player? = null
    var currentPlayerIndex = 0


    fun eliminatePlayer (player:Player){
        player.isAlive = false
        println ("${player.name} has been eliminated!")
    }

    fun playerByIndex(): Player {
        return players[currentPlayerIndex]
    }

    //Wird zu Beginn des Spielzugs aufgerufen
    fun startTurn(player: Player) {
        currentPlayer = player
        println("Welche Karte möchtest du spielen? (0 bis ${player.hand.lastIndex})")
        val index = readLine()?.toIntOrNull()

        if (index != null && index in player.hand.indices) {
            val card = player.hand[index]
            val success = cardManager.playCard(card, player)
            if (success) {
                println("Du spielst: ${card.type}")
            }
        } else {
            println("Keine Karte gespielt. Du kannst nun eine Karte ziehen.")
        }
    }

    //Wird aufgerufen, wenn Spieler auf Ziehstapel klickt
    fun endTurn() {
        val player = currentPlayer ?: return

        //Karte ziehen
        val drawn = cardManager.drawCard()
        if (drawn != null) {

        } else {
            println("${player.name} konnte keine Karte ziehen – Deck ist leer.")
        }

        println("---- ${player.name}'s Zug wurde beendet ----")
        currentPlayer = null
    }
}
