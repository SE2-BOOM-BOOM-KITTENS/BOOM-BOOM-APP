package com.example.boomboomfrontend.viewmodel

import androidx.lifecycle.ViewModel
import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.Player
import com.example.boomboomfrontend.network.messages.GameStateNetworkPacket
import com.example.boomboomfrontend.network.websocket.Callbacks
import java.util.UUID

class GameStateViewModel :ViewModel() ,Callbacks {

    lateinit var playerId: UUID
    lateinit var playerName: String
    lateinit var lobbyId: UUID
    lateinit var opponents: MutableList<Player>
    lateinit var cardHand: MutableList<Card>
    var gameOver: Boolean = false
    var gameReady: Boolean = false

    init {
        playerName = "Steve"
    }

    fun updateGameState(newGameState: GameStateNetworkPacket){

    }

    fun gameIsReady(){
        gameReady = true
    }

    override fun onResponse(res: String) {
        TODO("Not yet implemented")
    }

}