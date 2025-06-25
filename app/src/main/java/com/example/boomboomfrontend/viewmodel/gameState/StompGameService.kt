package com.example.boomboomfrontend.viewmodel.gameState

import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.network.messages.PlayerMessage
import com.example.boomboomfrontend.network.websocket.Callbacks
import com.example.boomboomfrontend.network.websocket.Stomp
import org.json.JSONObject

class StompGameService(callback: (String) -> Unit) {

    init {
        Stomp.setCallbacks(Callbacks { res -> callback(res) })
    }

    suspend fun disconnect(){
        val playerMessage = PlayerMessage(action = "EXIT")
        Stomp.sendAction(playerMessage)
        Stomp.disconnect()
    }

    fun playCard(card: Card){
        val cardJson = JSONObject().apply {
            put("name", card.name)
            put("type", card.type)
        }
        val playerMessage = PlayerMessage(action = "PLAY_CARDS", payload = cardJson)
        Stomp.sendAction(playerMessage)
    }

    fun getHand(){
        val playerMessage = PlayerMessage(action = "HAND")
        Stomp.sendAction(playerMessage)
    }

    fun pass(){
        val playerMessage = PlayerMessage(action = "PASS")
        Stomp.sendAction(playerMessage)
    }

    fun joinGame(playerMessage: PlayerMessage){
        Stomp.joinGame(playerMessage)
    }

    fun initGame(){
        val playerMessage = PlayerMessage(action = "INIT")
        Stomp.sendAction(playerMessage)
    }

    fun explode(){
        val playerMessage = PlayerMessage(action = "EXPLODE")
        Stomp.sendAction(playerMessage)
    }

    fun shuffleDrawPile() {
        val playerMessage = PlayerMessage(action = "SHUFFLE")
        Stomp.sendAction(playerMessage)
    }
}