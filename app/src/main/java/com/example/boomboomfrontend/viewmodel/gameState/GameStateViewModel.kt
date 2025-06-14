package com.example.boomboomfrontend.viewmodel.gameState

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.boomboomfrontend.network.messages.PlayerMessage
import com.example.boomboomfrontend.network.websocket.Callbacks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameStateViewModel :ViewModel() ,Callbacks {

    private val stompService = StompService(this)
    val clientInfo = ClientInfo()
    val repository = GameStateRepository(clientInfo)

    private val _responseMessage = MutableStateFlow("")
    val responseMessage: StateFlow<String> = _responseMessage

    init {
        stompService.connect(clientInfo.playerName){
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
        stompService.getHand(clientInfo.playerName)
    }

    fun pass(playerMessage: PlayerMessage){
        stompService.sendAction(playerMessage)
        stompService.getHand(clientInfo.playerName)
    }

    fun joinGame(){
        val playerMessage = PlayerMessage(clientInfo.playerName,null,null)
        stompService.joinGame(playerMessage)
    }

    fun explode(){
        stompService.explode(clientInfo.playerName)
    }

    fun handleExplosion(){

    }


}