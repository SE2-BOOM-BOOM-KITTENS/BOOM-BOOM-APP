package com.example.boomboomfrontend.viewmodel.gameState

import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.network.messages.PlayerMessage
import com.example.boomboomfrontend.network.websocket.Callbacks
import com.example.boomboomfrontend.network.websocket.Stomp

class StompService(callbacks: Callbacks) {
    // fixme instead of passing this whole object just pass the callback method (after annotating the interface as functional)
    private val stomp = Stomp(callbacks)

    fun connect(onConnect: ()->Unit){
        stomp.connect(onConnect)
    }

    suspend fun disconnect(){
        val playerMessage = PlayerMessage(action = "EXIT")
        stomp.sendAction(playerMessage)
        stomp.disconnect()
    }

    fun playCard(list:MutableList<Card>?){
        val playerMessage = PlayerMessage(action = "PLAY_CARDS", payload = list)
        stomp.sendAction(playerMessage)
    }

    fun getHand(){
        val playerMessage = PlayerMessage(action = "HAND")
        stomp.sendAction(playerMessage)
    }

    fun pass(){
        val playerMessage = PlayerMessage(action = "PASS")
        stomp.sendAction(playerMessage)
    }

    fun joinGame(playerMessage: PlayerMessage){
        stomp.joinGame(playerMessage)
    }

    fun explode(){
        val playerMessage = PlayerMessage(action = "EXPLODE")
        stomp.sendAction(playerMessage)
    }

    fun sendCombo(list: List<Card>) {   // ✅ NEU!
        val playerMessage = PlayerMessage(action = "catComboPlayed", payload = list)
        stomp.sendAction(playerMessage)
    }
}