package com.example.boomboomfrontend.logic

import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.Player
import com.example.boomboomfrontend.model.CardType

class GameManager(
    private val cardManager: CardManager,
    private val players: MutableList<Player>
) {

    var currentPlayer: Player? = null
    var currentPlayerIndex = 0
  
      fun addPlayer (player: Player){
        players.add(player)
    }

    fun eliminatePlayer (player:Player){
        player.isAlive = false
        println ("${player.name} has been eliminated!")
    }

    fun getCurrentPlayer(): Player {
        return players[currentPlayerIndex]
    }

    //Wird zu Beginn des Spielzugs aufgerufen
    fun startTurn(player: Player) {
        currentPlayer = player
        println("Welche Karte möchtest du spielen? (0 bis ${player.hand.lastIndex})")
        val index = readLine()?.toIntOrNull()

        if (index != null && player.hand?.indices?.contains(index) == true ) {
            val card = player.hand?.get(index)
            val success = card?.let { cardManager.playCard(it, player, this) }
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

        val effect = CardEffectRegistry.getEffect(drawn.type)
        effect.apply(player, this)

        println("---- ${player.name}'s Zug wurde beendet ----")
        currentPlayer = null
    }
}
