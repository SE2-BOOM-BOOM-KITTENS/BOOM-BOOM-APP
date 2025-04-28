import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.example.boomboomfrontend.logic.*
import com.example.boomboomfrontend.model.*

class GameManagerTest {

    private lateinit var gameManager: GameManager
    private lateinit var cardManager: CardManager
    private lateinit var testPlayer: Player

    @BeforeEach
    fun setup() {
        cardManager = CardManager()
        testPlayer = Player(id = "1", name = "Test", hand = mutableListOf())
        gameManager = GameManager(cardManager, mutableListOf(testPlayer))
    }

    @Test
    fun `eliminierter Spieler wird als nicht lebendig markiert`() {
        assertTrue(testPlayer.isAlive)
        gameManager.eliminatePlayer(testPlayer)
        assertFalse(testPlayer.isAlive)
    }

    @Test
    fun `playerByIndex gibt den richtigen Spieler zurueck`() {
        val result = gameManager.playerByIndex()
        assertEquals(testPlayer, result)
    }

    @Test
    fun `endTurn wendet Effekt an und setzt currentPlayer zurueck`() {
        gameManager.currentPlayer = testPlayer
        cardManager.returnCardToDeckAt(Card(CardType.BLANK), 0)

        gameManager.endTurn()

        assertNull(gameManager.currentPlayer)
    }

    @Test
    fun `startTurn ohne Eingabe veraendert Hand nicht`() {
        testPlayer.hand.add(Card(CardType.BLANK))
        val initialHandSize = testPlayer.hand.size

        gameManager.startTurn(testPlayer) // Simuliert keine Eingabe

        assertEquals(initialHandSize, testPlayer.hand.size)
    }

    /*@Test
    fun `Exploding Kitten mit Defuse wird zurückgelegt`() {
        //wird noch hinzugefügt
    }*/
}
