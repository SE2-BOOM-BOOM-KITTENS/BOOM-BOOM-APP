package com.example.boomboomfrontend.viewmodel.gameState

import com.example.boomboomfrontend.network.messages.PlayerMessage
import com.example.boomboomfrontend.network.websocket.Callbacks
import com.example.boomboomfrontend.network.websocket.Stomp

class StompService(callbacks: Callbacks) {
    // fixme instead of passing this whole object just pass the callback method (after annotating the interface as functional)
    private val stomp = Stomp(callbacks)

    fun connect(playerName: String, onConnect: ()->Unit){
        stomp.connect(playerName,onConnect)
    }

    fun sendAction(playerMessage:PlayerMessage){
        stomp.sendAction(playerMessage)
    }

    fun getHand(playerName: String){
        stomp.getHand(playerName)
    }

    fun joinGame(playerMessage: PlayerMessage){
        stomp.joinGame(playerMessage)
    }

    fun explode(playerName: String){
        stomp.explode(playerName)
    }
}