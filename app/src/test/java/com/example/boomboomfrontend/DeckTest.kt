package com.example.boomboomfrontend

import com.example.boomboomfrontend.model.*
import org.junit.jupiter.api.BeforeEach
import kotlin.test.*

class DeckTest {

    private lateinit var deck: Deck
    private lateinit var players: List<Player>

    @BeforeEach
    fun setup() {
        deck = Deck()
        players = listOf(
            Player("1", "Alice"),
            Player("2", "Bob"),
            Player("3", "Charlie")
        )
    }

    @Test
    fun `initialize fills the deck with correct number of cards`() {
        deck.initialize(players)

        // Expect 2 exploding kittens (3 players - 1), 3 defuse, 10 blank cards
        assertEquals(15, deck.size())
    }

    @Test
    fun `draw removes and returns the top card`() {
        deck.initialize(players)
        val sizeBefore = deck.size()
        val card = deck.draw()

        assertNotNull(card)
        assertEquals(sizeBefore - 1, deck.size())
    }

    @Test
    fun `draw returns null when deck is empty`() {
        val emptyDraw = deck.draw()
        assertNull(emptyDraw)
    }

    @Test
    fun `insertAt inserts card at valid index`() {
        val card = Card("TestCard", CardType.NOPE)
        deck.initialize(players)
        val initialSize = deck.size()

        deck.insertAt(card, 0)
        assertEquals(initialSize + 1, deck.size())
    }

    @Test
    fun `insertAt with out-of-range index appends card at end`() {
        val card = Card("EndCard", CardType.NOPE)
        deck.insertAt(card, 999)

        assertEquals(1, deck.size())
        assertEquals(card, deck.draw())
    }

    @Test
    fun `insertAt with negative index inserts at start`() {
        val card = Card("StartCard", CardType.NOPE)
        deck.insertAt(card, -5)

        assertEquals(1, deck.size())
        assertEquals(card, deck.draw())
    }

    @Test
    fun `size returns number of cards in deck`() {
        deck.initialize(players)
        assertEquals(15, deck.size())
        deck.draw()
        assertEquals(14, deck.size())
    }
}