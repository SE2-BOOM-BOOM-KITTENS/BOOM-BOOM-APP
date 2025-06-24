package com.example.boomboomfrontend.viewmodel.gameState

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boomboomfrontend.model.Player
import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.network.messages.PlayerMessage
import com.example.boomboomfrontend.network.websocket.Callbacks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameStateViewModel :ViewModel() ,Callbacks {

    private val stompGameService = StompGameService { res -> onResponse(res) }
    val repository = GameStateRepository()
    val clientInfo = ClientInfoHolder.clientInfo

    private val _responseMessage = MutableStateFlow("")
    val responseMessage: StateFlow<String> = _responseMessage

    init {
        val stackTrace = Throwable().stackTrace
        val caller = stackTrace.getOrNull(1)
        println("Instantiated by: ${caller?.className}.${caller?.methodName}")
        stompGameService.initGame()
//        stompGameService.connect(){
            Log.i("ViewModel","Trying to connect to Server; LobbyId: ${clientInfo.currentLobbyID}")
            stompGameService.getHand()
//        }
    }

    override fun onResponse(res: String) {
        if(res == "Disconnected"){
            clientInfo.currentLobbyID = null
            return
        }
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

    fun playCard(card: Card){
        stompGameService.playCard(card)
        stompGameService.getHand()
    }

    fun pass(){
        stompGameService.pass()
        stompGameService.getHand()
    }

    fun joinGame(){
        val playerMessage = PlayerMessage(clientInfo.playerName,null,clientInfo.playerId)
        stompGameService.joinGame(playerMessage)
    }

    fun exit(){
        viewModelScope.launch {
            stompGameService.disconnect()
        }
    }

    fun explode(){
        stompGameService.explode()
    }

    fun handleExplosion(){

    }

    fun setPlayersFromLobby(playersFromLobby: List<Player>) {
        repository.players.clear()
        repository.players.addAll(playersFromLobby)
    }



}