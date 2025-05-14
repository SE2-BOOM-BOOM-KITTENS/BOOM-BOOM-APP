package com.example.boomboomfrontend.logic.cardlogic

import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.CardType
import com.example.boomboomfrontend.network.messages.CatComboMessage
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class CatComboClientLogicTest {

    @Test
    fun `detectCatCombo returns RANDOM for 2 same cards`() {
        val cards = listOf(
            Card("C1", CardType.CAT_TACO),
            Card("C2", CardType.CAT_TACO)
        )
        val result = detectCatCombo(cards)
        assertEquals(CatComboType.RANDOM, result)
    }

    @Test
    fun `detectCatCombo returns SPECIFIC for 3 same cards`() {
        val cards = List(3) { Card("C$it", CardType.CAT_BEARD) }
        val result = detectCatCombo(cards)
        assertEquals(CatComboType.SPECIFIC, result)
    }

    @Test
    fun `detectCatCombo returns DISCARD for 5 different cards`() {
        val cards = listOf(
            Card("1", CardType.CAT_TACO),
            Card("2", CardType.CAT_BEARD),
            Card("3", CardType.CAT_HAIRY_POTATO),
            Card("4", CardType.CAT_RAINBOW_RALPHING),
            Card("5", CardType.CAT_CATERMELON)
        )
        val result = detectCatCombo(cards)
        assertEquals(CatComboType.DISCARD, result)
    }

    @Test
    fun `detectCatCombo returns NONE for invalid combo`() {
        val cards = listOf(
            Card("A", CardType.CAT_TACO),
            Card("B", CardType.CAT_BEARD)
        )
        val result = detectCatCombo(cards)
        assertEquals(CatComboType.NONE, result)
    }

    @Test
    fun `sendCatCombo sends correct JSON via WebSocketClient`() {
        val socket = mock<WebSocketClient>()

        val cards = listOf(
            Card("C1", CardType.CAT_CATERMELON),
            Card("C2", CardType.CAT_CATERMELON)
        )

        sendCatCombo(socket, cards)

        val expectedMessage = CatComboMessage(
            action = "catComboPlayed",
            cardIds = listOf("C1", "C2")
        )
        val expectedJson = Json.encodeToString(CatComboMessage.serializer(), expectedMessage)

        verify(socket).send(expectedJson)
    }
}
