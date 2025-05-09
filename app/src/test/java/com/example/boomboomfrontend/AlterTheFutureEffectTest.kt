package com.example.boomboomfrontend
import com.example.boomboomfrontend.logic.CardManager
import com.example.boomboomfrontend.logic.GameManager
import com.example.boomboomfrontend.logic.effects.AlterTheFutureEffect
import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.Player
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.LinkedList

class AlterTheFutureEffectTest(cardManager: CardManager) {

    private lateinit var gameManager: GameManager
    private lateinit var player: Player

    @BeforeEach
    fun setup() {
        gameManager = object : GameManager() {
            override fun promptPlayerToReorder(player: Player, cards: List<Card>): List<Card> {
                return cards.reversed() // Für den Test: Karten werden einfach umgedreht
            }
        }
        player = Player(name = "TestPlayer", id = TODO())

        // drawPile vorbereiten
        val field = GameManager::class.java.getDeclaredField("drawPile")
        field.isAccessible = true
        field.set(gameManager, LinkedList<Card>().apply {
            add(Card(type = TODO()))
            add(Card(type = TODO()))
            add(Card(type = TODO()))
            add(Card(type = TODO()))
        })
    }

    @Test
    fun `should allow player to rearrange top 3 cards`() {
        val effect = AlterTheFutureEffect()
        effect.apply(player, gameManager)

        val top = gameManager.peekTopCards(3)
        assert(top.map { it.type } == listOf("Card3", "Card2", "Card1"))
    }
}