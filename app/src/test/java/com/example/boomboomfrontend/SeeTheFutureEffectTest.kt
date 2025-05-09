package com.example.boomboomfrontend
import com.example.boomboomfrontend.logic.GameManager
import com.example.boomboomfrontend.logic.effects.SeeTheFutureEffect
import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.Player
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.LinkedList

class SeeTheFutureEffectTest {

    private lateinit var gameManager: GameManager
    private lateinit var player: Player

    @BeforeEach
    fun setup() {
        gameManager = GameManager(cardManager = TODO(), players = TODO())
        player = Player(
            name = "TestPlayer", id = TODO())

        // Stapel vorbereiten
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
    fun `should show top 3 cards to player`() {
        val effect = SeeTheFutureEffect()
        effect.apply(player, gameManager)

        val top = gameManager.peekTopCards(3)
        assert(top.map { it.type } == listOf("Card1", "Card2", "Card3"))
    }
}