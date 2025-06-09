package com.example.boomboomfrontend.logic.cardlogic

import com.example.boomboomfrontend.network.messages.CatComboMessage
import kotlinx.serialization.json.Json

interface WebSocketClient {
    fun send(message: String) {}

    fun sendJson(message: CatComboMessage) {
        val json = Json.encodeToString(CatComboMessage.serializer(), message)
        send(json)
    }
}
