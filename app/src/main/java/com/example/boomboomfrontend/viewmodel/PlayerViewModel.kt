package com.example.boomboomfrontend.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boomboomfrontend.model.Player
import com.example.boomboomfrontend.model.PlayerResponse
import com.example.boomboomfrontend.network.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerViewModel : ViewModel() {
    private val repository = ApiRepository()

    private val _players = MutableStateFlow<List<PlayerResponse>>(emptyList())
    val players: StateFlow<List<PlayerResponse>> = _players


    private val _responseMessage = MutableStateFlow("")
    val responseMessage: StateFlow<String> = _responseMessage

    fun addPlayer(name: String) {
        viewModelScope.launch {
            val response = repository.addPlayer(
                Player(id = null, name = name, status = null)
            )
            if (response.isSuccessful) {
                _responseMessage.value = response.body()?.string() ?: "Leere Antwort"

            } else {
                _responseMessage.value = "Fehler: ${response.code()}"
            }
        }
    }

    fun getAllPlayers() {
        viewModelScope.launch {
            val response = repository.getAllPlayers()
            if (response.isSuccessful) {
                _players.value = response.body() ?: emptyList()
                Log.d("PlayersResponse", players.toString())
            } else {
                Log.e("PlayersResponse", "Fehler: ${response.code()}")
            }

        }
    }

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

}
