package com.example.boomboomfrontend.viewmodel.gameState

import android.util.Log
import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.network.messages.PlayerMessage
import com.example.boomboomfrontend.network.websocket.Callbacks
import com.example.boomboomfrontend.network.websocket.Stomp

class StompGameService(callbacks: Callbacks) {
    // fixme instead of passing this whole object just pass the callback method (after annotating the interface as functional)
    init {
        Stomp.setCallbacks(callbacks)
    }

    suspend fun disconnect(){
        val playerMessage = PlayerMessage(action = "EXIT")
        Stomp.sendAction(playerMessage)
        Stomp.disconnect()
    }

    fun playCard(card: Card){
        val playerMessage = PlayerMessage(action = "PLAY_CARDS", payload = card)
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