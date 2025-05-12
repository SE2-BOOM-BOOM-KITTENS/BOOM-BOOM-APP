// CatComboClientLogic.kt

package com.example.boomboomfrontend.logic.cardlogic

import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.CardType
import kotlinx.serialization.encodeToString
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
    val message = mapOf(
        "action" to "catComboPlayed",
        "cardIds" to selectedCards.map { it.id }
    )
    socket.sendJson(message)
}

fun respondToSpecificSteal(socket: WebSocketClient, targetId: String, cardType: CardType) {
    val response = mapOf(
        "action" to "resolveSpecificSteal",
        "targetPlayerId" to targetId,
        "cardType" to cardType.name
    )
    socket.sendJson(response)
}

fun respondToDiscardSelection(socket: WebSocketClient, selectedType: CardType) {
    val response = mapOf(
        "action" to "resolveDiscardSelection",
        "selectedCardType" to selectedType.name
    )
    socket.sendJson(response)
}

// Extension function to simplify JSON sending
fun WebSocketClient.sendJson(data: Any) {
    val json = Json.encodeToString(data)
    this.send(json)
}

// Dummy WebSocketClient interface for reference
interface WebSocketClient {
    fun send(text: String)
}
