package com.example.boomboomfrontend.logic

import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.Deck
import com.example.boomboomfrontend.model.Player

class CardManager {

    // fixme dont forget to send deck updates to the server
    private val deck = Deck()
    private val discardPile: MutableList<Card> = mutableListOf()

    fun initializeDeck(players: List<Player>) {
        deck.initialize(players)                            // Deck wird vorbereitet
    }

    fun drawCard(): Card? = deck.draw()

    fun playCard(card: Card, player: Player): Boolean {
        return if (player.hand.remove(card)) {
            discardPile.add(card)

            // Neue Logik: Effekt aus Registry holen und ausführen
            //val effect = CardEffectRegistry.getEffect(card.type)
            //effect.apply(player, gameManager)

            true
        } else false
    }

    fun returnCardToDeckAt(card: Card, position: Int) {
        deck.insertAt(card, position)
    }


    fun getDiscardPile(): List<Card> = discardPile
}

