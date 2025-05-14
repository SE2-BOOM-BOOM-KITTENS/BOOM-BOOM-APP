package com.example.boomboomfrontend

/*import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.example.boomboomfrontend.logic.*
import com.example.boomboomfrontend.model.*

class CardManagementTest {

    private lateinit var gameManager: GameManager
    private lateinit var cardManager: CardManager
    private lateinit var players: MutableList<Player>
    private lateinit var testPlayer: Player

    @BeforeEach
    fun setup() {
        //Spieler mit einer BLANK-Karte ausstatten
        testPlayer = Player(
            id = "1",
            name = "Test",
            status = ConnectionStatus.JOINED,
            hand = mutableListOf()
        )

        players = mutableListOf(testPlayer)
        players = mutableListOf(testPlayer)
        cardManager = CardManager()
        cardManager.initializeDeck(players)
        gameManager = GameManager(cardManager, players)
    }

    @Test
    fun `drawCard returns null if deck is empty`() {
        // Leere das Deck komplett
        while (cardManager.drawCard() != null) {}

        val result = cardManager.drawCard()
        assertNull(result)
    }

    @Test
    fun `playCard removes from hand and adds to discardPile`() {
        val card = Card(CardType.BLANK)
        testPlayer.hand.add(card)

        val result = cardManager.playCard(card, testPlayer, gameManager)

        assertTrue(result, "Card should be successfully played")
        assertFalse(testPlayer.hand.contains(card), "Card should be removed from hand")
        assertTrue(cardManager.getDiscardPile().contains(card), "Card should be in discard pile")
    }
}*/