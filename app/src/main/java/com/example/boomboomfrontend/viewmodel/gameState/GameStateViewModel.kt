package com.example.boomboomfrontend.viewmodel.gameState

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.boomboomfrontend.network.messages.PlayerMessage
import com.example.boomboomfrontend.network.websocket.Callbacks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameStateViewModel :ViewModel() ,Callbacks {

    private val stompService = StompService(this)
    val repository = GameStateRepository()
    val clientInfo = ClientInfoHolder.clientInfo

    private val _responseMessage = MutableStateFlow("")
    val responseMessage: StateFlow<String> = _responseMessage

    init {
        stompService.connect(){
            Log.i("ViewModel","Trying to connect to Server; LobbyId: ${clientInfo.currentLobbyID}")
            joinGame()
        }
    }

    override fun onResponse(res: String) {
        try{
            val(type, message, gameStateJson) = repository.processServerMessage(res)
            _responseMessage.value = message

            when(type){
                "GAME_STATE" -> repository.updateState(gameStateJson)
                "HAND" -> {
                    Log.i("Hand","updating Hand")
                    repository.updateHand(gameStateJson)
                    if(repository.checkForExplode()) explode()
                }
                "EXPLODE" -> handleExplosion()
            }
        } catch (e:Exception){
            Log.e("GameStateError", "Failed Updating GameState: ${e.localizedMessage}")
        }
    }

    fun playCard(playerMessage: PlayerMessage){
        stompService.sendAction(playerMessage)
        stompService.getHand()
    }

    fun pass(playerMessage: PlayerMessage){
        stompService.sendAction(playerMessage)
        stompService.getHand()
    }

    fun joinGame(){
        val playerMessage = PlayerMessage(clientInfo.playerName,null,clientInfo.playerId)
        stompService.joinGame(playerMessage)
    }

    fun explode(){
        stompService.explode()
    }

    fun handleExplosion(){

    }


}