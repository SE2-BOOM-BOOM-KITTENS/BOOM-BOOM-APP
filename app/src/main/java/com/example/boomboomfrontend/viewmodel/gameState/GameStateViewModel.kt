package com.example.boomboomfrontend.viewmodel.gameState

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boomboomfrontend.model.Player
import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.network.websocket.Callbacks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameStateViewModel :ViewModel() ,Callbacks {

    private val stompGameService = StompGameService { res -> onResponse(res) }
    val repository = GameStateRepository()
    val clientInfo = ClientInfoHolder.clientInfo
    var lockButtons = false
    var explodeDialog = mutableStateOf(false)

    private val _responseMessage = MutableStateFlow("")
    val responseMessage: StateFlow<String> = _responseMessage

    init {
        val stackTrace = Throwable().stackTrace
        val caller = stackTrace.getOrNull(1)
        println("Instantiated by: ${caller?.className}.${caller?.methodName}")
        stompGameService.initGame()
            Log.i("ViewModel","Trying to connect to Server; LobbyId: ${clientInfo.currentLobbyID}")
            stompGameService.getHand()
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
                "GAME_STATE" -> {
                    repository.updateState(gameStateJson)
                    stompGameService.getHand()
                }
                "HAND" -> {
                    Log.i("Hand","updating Hand")
                    repository.updateHand(gameStateJson)
                    if(repository.checkForExplode()) explode()
                    lockButtons = false
                }
                "TIMEOUT" -> {
                    Log.i("timeout","Current Player Timed out")
                }
                "EXPLODE" -> handleExplosion()
            }
        } catch (e:Exception){
            Log.e("GameStateError", "Failed Updating GameState: ${e.localizedMessage}")
        }
    }

    fun playCard(card: Card){
        lockButtons = true
        stompGameService.playCard(card)
    }

    fun pass(){
        stompGameService.pass()
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
//        if(this.explodeDialog){
//           explode()
//        }
    }

    fun setPlayersFromLobby(playersFromLobby: List<Player>) {
        repository.players.clear()
        repository.players.addAll(playersFromLobby)
    }
}