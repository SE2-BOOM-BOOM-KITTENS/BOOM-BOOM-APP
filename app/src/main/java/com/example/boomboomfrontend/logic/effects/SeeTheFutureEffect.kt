package com.example.boomboomfrontend.logic.effects

import com.example.boomboomfrontend.logic.CardEffect
import com.example.boomboomfrontend.logic.GameManager
import com.example.boomboomfrontend.model.Player

class SeeTheFutureEffect : CardEffect {
    override fun apply(player: Player, gameManager: GameManager) {
        val topCards = gameManager.peekTopCards(3)
        gameManager.showFutureCardsToPlayer(player, topCards)
        println("${player.name} sees the top 3 cards: $topCards")
    }
}