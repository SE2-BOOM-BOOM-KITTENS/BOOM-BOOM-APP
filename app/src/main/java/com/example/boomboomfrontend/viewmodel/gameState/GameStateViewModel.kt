package com.example.boomboomfrontend.viewmodel.gameState

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boomboomfrontend.model.Player
import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.CardType
import com.example.boomboomfrontend.network.messages.PlayerMessage
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
        stompGameService.initGame()
            Log.i("ViewModel","Trying to connect to Server; LobbyId: ${clientInfo.currentLobbyID}")
            stompGameService.getHand()
    }

    override fun onResponse(res: String) {
        if(res == "Disconnected"){
            Log.i("Disconnected","Disconnected")
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
                    Log.i("Hand","Unlocking buttons")
                    lockButtons = false
                }
                "CHEAT" -> {
                    Log.i("Hand","updating Cheated Hand")
                    repository.updateHand(gameStateJson)
                    Log.i("Hand","Unlocking buttons")
                    lockButtons = false
                }
                "TIMEOUT" -> {
                    repository.updateState(gameStateJson)
                    if(repository.myTurn){
                        Log.i("Hand","Player timed out")
                    } else{
                        repository.updateState(gameStateJson)
                    }
                }
                "EXPLODE" -> handleExplosion()
                "SHUFFLE" -> shuffleDrawPile()
            }
        } catch (e:Exception){
            Log.e("GameStateError", "Failed Updating GameState: ${e.localizedMessage}")
        }
    }

    fun getLastCardPlayedName(){
        repository.cardPlayed!!.name
    }

    fun playCard(card: Card){
        lockButtons = true
        stompGameService.playCard(card)

        if (card.type == CardType.SHUFFLE) {
            stompGameService.shuffleDrawPile()
        }
        stompGameService.getHand()

    }

    fun pass(){
        stompGameService.pass()
    }

    fun exit(){
        viewModelScope.launch {
            stompGameService.disconnect()
        }
    }

    fun cheat(card: Card){
        lockButtons = true
        stompGameService.cheat(card)
    }

    fun checkCheat(){
        Log.i("ACCUSE","BEGINNING ACCUSATION")
        val card = repository.cardPlayed
        if(card != null)
            stompGameService.checkCheat(card)
    }

    fun explode(){
        stompGameService.explode()
    }

    fun handleExplosion(){
        explodeDialog.value = true
    }

    fun setPlayersFromLobby(playersFromLobby: List<Player>) {
        repository.players.clear()
        repository.players.addAll(playersFromLobby)
    }


    private fun shuffleDrawPile() {
        repository.notifyShuffle()
        _responseMessage.value = "Deck shuffled"
    }





}