package com.example.boomboomfrontend.model

class Deck {

    private val cards: MutableList<Card> = mutableListOf()

    fun initialize(players: List<Player>) {
        cards.clear()

        // Beispielhaftes Setup für Sprint 1
        repeat(players.size - 1) { cards.add(Card("Exploding Kitten", CardType.EXPLODING_KITTEN)) }
        repeat(players.size) { cards.add(Card("Defuse", CardType.DEFUSE)) }
        repeat(10) { cards.add(Card("Blank", CardType.BLANK)) }

        cards.shuffle()
    }

    fun draw(): Card? = if (cards.isNotEmpty()) cards.removeFirst() else null

    fun insertAt(card: Card, index: Int) {
        cards.add(index.coerceIn(0, cards.size), card)
    }

    fun size(): Int = cards.size
}
