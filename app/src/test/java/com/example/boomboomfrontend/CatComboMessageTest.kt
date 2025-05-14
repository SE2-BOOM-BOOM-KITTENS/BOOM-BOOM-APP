import com.example.boomboomfrontend.network.messages.CatComboMessage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CatComboMessageTest {

    @Test
    fun `serialization and deserialization works`() {
        val original = CatComboMessage(
            action = "catComboPlayed",
            cardIds = listOf("C1", "C2")
        )

        val json = Json.encodeToString(original)
        val decoded = Json.decodeFromString<CatComboMessage>(json)

        assertEquals(original, decoded)
    }
}
