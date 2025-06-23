package com.example.boomboomfrontend.viewmodel.gameState

import com.example.boomboomfrontend.network.messages.PlayerMessage
import com.example.boomboomfrontend.network.websocket.Callbacks
import com.example.boomboomfrontend.network.websocket.Stomp

class StompService(callbacks: Callbacks) {
    // fixme instead of passing this whole object just pass the callback method (after annotating the interface as functional)
    private val stomp = Stomp(callbacks)

    fun connect(onConnect: ()->Unit){
        stomp.connect(onConnect)
    }

    fun disconnect(){
        val playerMessage = PlayerMessage(action = "EXIT")
        stomp.sendAction(playerMessage)
    }

    fun sendAction(playerMessage:PlayerMessage){
        stomp.sendAction(playerMessage)
    }

    fun getHand(){
        val playerMessage = PlayerMessage(action = "HAND")
        stomp.sendAction(playerMessage)
    }

    fun joinGame(playerMessage: PlayerMessage){
        stomp.joinGame(playerMessage)
    }

    fun explode(){
        stomp.explode(PlayerMessage())
    }
}