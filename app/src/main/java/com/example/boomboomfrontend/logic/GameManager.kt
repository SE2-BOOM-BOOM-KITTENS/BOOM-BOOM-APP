package com.example.boomboomfrontend.logic

import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.Player
import com.example.boomboomfrontend.model.CardType
import java.util.LinkedList

open class GameManager(
    private val cardManager: CardManager,
    private val players: MutableList<Player>
) {

    var currentPlayer: Player? = null
    var currentPlayerIndex = 0
    val drawPile: LinkedList<Card> = LinkedList()
  
      fun addPlayer (player: Player){
        players.add(player)
    }

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
            val success = cardManager.playCard(card, player, this)
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
            val effect = CardEffectRegistry.getEffect(drawn.type)
            effect.apply(player, this)
        } else {
            println("${player.name} konnte keine Karte ziehen – Deck ist leer.")
        }

        println("---- ${player.name}'s Zug wurde beendet ----")
        currentPlayer = null
    }
    fun peekTopCards(count: Int): List<Card> {
        return drawPile.take(count)
    }
    fun showFutureCardsToPlayer(player: Player, cards: List<Card>) {
        println("👁 ${player.name} sees the future:")
        cards.forEachIndexed { index, card ->
            println("${index + 1}: ${card.type}")
        }
    }
    open fun promptPlayerToReorder(player: Player, cards: List<Card>): List<Card> {
        println("🔁 ${player.name}, please enter the new order of the top cards (comma-separated indices)")
        cards.forEachIndexed { index, card ->
            println("${index + 1}: ${card.type}")
        }
        return cards.reversed()
    }
    fun setTopCards(newCards: List<Card>) {
        if (newCards.size > drawPile.size) {
            throw IllegalArgumentException("New card list is longer than the current draw pile.")
        }

        // Entferne die obersten Karten im Stapel
        repeat(newCards.size) {
            drawPile.removeFirst()
        }

        // Füge die neuen Karten in umgekehrter Reihenfolge oben auf den Stapel
        for (i in newCards.indices.reversed()) {
            drawPile.addFirst(newCards[i])
        }

        println("🔁 New top cards set: ${newCards.map { it.type }}")
    }
}
