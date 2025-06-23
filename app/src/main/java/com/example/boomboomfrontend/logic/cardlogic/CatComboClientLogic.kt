// CatComboClientLogic.kt

package com.example.boomboomfrontend.logic.cardlogic

import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.network.messages.CatComboMessage
import kotlinx.serialization.json.Json

enum class CatComboType {
    RANDOM, SPECIFIC, DISCARD, NONE
}

fun detectCatCombo(cards: List<Card>): CatComboType {
    val types = cards.map { it.type }
    val unique = types.toSet()

    return when {
        cards.size == 2 && unique.size == 1 -> CatComboType.RANDOM
        cards.size == 3 && unique.size == 1 -> CatComboType.SPECIFIC
        cards.size == 5 && unique.size == 5 -> CatComboType.DISCARD
        else -> CatComboType.NONE
    }
}

fun sendCatCombo(socket: WebSocketClient, selectedCards: List<Card>) {
    val message = CatComboMessage(
        action = "catComboPlayed",
        cardIds = selectedCards.map { it.name }
    )
    socket.sendJson(message)
}

// Dummy interface for WebSocket sending
interface WebSocketClient {
    fun send(text: String)

    fun sendJson(message: CatComboMessage) {
        val json = Json.encodeToString(CatComboMessage.serializer(), message)
        send(json)
    }
}