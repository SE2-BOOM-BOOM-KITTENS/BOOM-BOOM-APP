package com.example.boomboomfrontend.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boomboomfrontend.model.PlayerResponse
import com.example.boomboomfrontend.network.ApiRepository
import com.example.boomboomfrontend.network.messages.PlayerMessage
import com.example.boomboomfrontend.network.websocket.Callbacks
import com.example.boomboomfrontend.network.websocket.Stomp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class PlayerViewModel : ViewModel(), Callbacks {

    // API
    private val repository = ApiRepository()

    // Player List (REST)
    private val _players = MutableStateFlow<List<PlayerResponse>>(emptyList())
    val players: StateFlow<List<PlayerResponse>> = _players

    // Response Msg (WebSocket)
    private val _responseMessage = MutableStateFlow("")
    val responseMessage: StateFlow<String> = _responseMessage

    // STOMP WebSocket-Client
    init {
        Stomp.setCallbacks(this)
    }

    // API: add player
    fun addPlayer(name: String) {
        viewModelScope.launch {
            val response = repository.addPlayer(name)
            if (response.isSuccessful) {
                _responseMessage.value = response.body()?.string() ?: "Leere Antwort"
            } else {
                _responseMessage.value = "Fehler: ${response.code()}"
            }
        }
    }

    // API: show all players
    fun getAllPlayers() {
        viewModelScope.launch {
            val response = repository.getAllPlayers()
            if (response.isSuccessful) {
                _players.value = response.body() ?: emptyList()
                Log.d("PlayersResponse", _players.value.toString())
            } else {
                Log.e("PlayersResponse", "Fehler: ${response.code()}")
            }
        }
    }

    // API: get players in lobby
    fun getPlayersInLobby(lobbyId: String) {
        viewModelScope.launch {
            val response = repository.getPlayersInLobby(lobbyId)
            if (response.isSuccessful) {
                _players.value = response.body() ?: emptyList()
                Log.d("LobbyPlayers", "Spieler aus Lobby $lobbyId: ${_players.value}")
            } else {
                Log.e("LobbyPlayers", "Fehler: ${response.code()}")
            }
        }
    }


    // WebSocket: send Response from WebSocket
    fun sendResponseMessage() {
        Stomp.sendResponseMessage()
    }

    // WebSocket: disconnect
    fun disconnectWebSocket() {
        viewModelScope.launch {
        Stomp.disconnect()
            }
    }

    // Callback for WebSocket messages
    override fun onResponse(message: String) {
        _responseMessage.value = message
    }

    // after disconnect from WebSocket
    override fun onCleared() {
        super.onCleared()
        disconnectWebSocket()
    }

    fun testBroadcast(){
        val playerMessage = PlayerMessage()
        Stomp.sendDebugTest(playerMessage)
    }

    fun sendErrorAction(){
        val playerMessage = PlayerMessage()
        Stomp.sendErrorAction(playerMessage)
    }
}
