package com.example.boomboomfrontend.logic.effects

import com.example.boomboomfrontend.logic.CardEffect
import com.example.boomboomfrontend.logic.GameManager
import com.example.boomboomfrontend.model.Player

class AlterTheFutureEffect : CardEffect {
    override fun apply(player: Player, gameManager: GameManager) {
        val topCards = gameManager.peekTopCards(3)
        val newOrder = gameManager.promptPlayerToReorder(player, topCards)
        gameManager.setTopCards(newOrder)
        println("${player.name} altered the future: $newOrder")
    }
}