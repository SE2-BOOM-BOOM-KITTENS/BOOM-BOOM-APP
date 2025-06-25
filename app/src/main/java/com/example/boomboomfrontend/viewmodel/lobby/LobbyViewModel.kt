package com.example.boomboomfrontend.viewmodel.lobby

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boomboomfrontend.model.LobbyResponse
import com.example.boomboomfrontend.model.Player
import com.example.boomboomfrontend.model.PlayerResponse
import com.example.boomboomfrontend.network.CreateLobbyRequest
import com.example.boomboomfrontend.network.RetrofitInstance
import com.example.boomboomfrontend.network.messages.networkPackets.LobbyNetworkPacket
import com.example.boomboomfrontend.network.websocket.Callbacks
import com.example.boomboomfrontend.network.websocket.clientInfo
import com.example.boomboomfrontend.viewmodel.gameState.ClientInfoHolder

import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class LobbyViewModel : ViewModel(), Callbacks {

    private val stompLobbyService = StompLobbyService { res -> onResponse(res) }
    var goToGame by mutableStateOf(false);
    var goToLobby by mutableStateOf(false)
    val clientInfo = ClientInfoHolder.clientInfo

    // Lobby creation response
    private val _lobbyState = MutableStateFlow<LobbyNetworkPacket?>(null)
    val lobbyState: StateFlow<LobbyNetworkPacket?> = _lobbyState

    // Players in a lobby
    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players

    // All lobbies
    private val _lobbies = MutableStateFlow<Map<String, LobbyResponse>>(emptyMap())
    val lobbies: StateFlow<Map<String, LobbyResponse>> = _lobbies


    fun createGame(){
        stompLobbyService.createGame()
    }

    fun connect(){
        stompLobbyService.connect {
            Log.i("Lobby","Trying to connect to Server;")
        }
    }



    override fun onResponse(res: String) {
        Log.d("WebSocket", "Received message: $res")

        try {
            val type= stompLobbyService.processServerMessage(res)

            if(type == "GAME_CREATED"){
                goToGame = true
            }
        }catch (e:Exception){
            goToGame = false
            Log.e("LobbyViewModel","Failed reading message from server: ${e.localizedMessage}")
        }
    }

    fun createLobby(playerId: UUID, maxPlayers: Int = 4) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.createLobby(
                    CreateLobbyRequest(playerId, maxPlayers)
                )

                if (response.isSuccessful) {
                    _lobbyState.value = response.body()
                    Log.d("LobbyViewModel", "Lobby created: ${response.body()}")
                    clientInfo.currentLobbyID = response.body()?.lobbyId
                    goToLobby = true
                } else {
                    Log.e("LobbyError", "Code: ${response.code()}, Error: ${response.errorBody()?.string()}")
                    val json = Gson().toJson(CreateLobbyRequest(playerId, maxPlayers))
                    Log.d("CreateLobbyRequest_JSON", json)
                }
            } catch (e: Exception) {
                Log.e("Lobby Error", "Exception during createLobby: ${e.message}")
            }
        }
    }

    fun getPlayersInLobby(lobbyId: UUID) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getPlayersInLobby(lobbyId.toString())
                if (response.isSuccessful) {
                    val playerResponses: List<PlayerResponse> = response.body() ?: emptyList()
                    _players.value = playerResponses.map { it.toPlayer() }
                    Log.d("LobbyViewModel", "Players in lobby: ${_players.value.map { it.name }}")
                } else {
                    Log.e("Lobby Error", "Failed to get players: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Lobby Error", "Exception during getPlayersInLobby: ${e.message}")
            }
        }
    }

    fun getAllLobbies() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getAllLobbies()
                if (response.isSuccessful) {
                    _lobbies.value = response.body() ?: emptyMap()
                    Log.d("LobbyViewModel", "Creators: ${_lobbies.value.values.map { it.creator.name }}")
                } else {
                    Log.e("Lobby Error", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Lobby Error", "Exception: ${e.message}")
            }
        }
    }

    fun joinLobby(lobbyId: String, playerId: UUID?) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.joinLobby(lobbyId, playerId)
                if (response.isSuccessful) {
                    Log.d("LobbyViewModel", "Successfully joined lobby $lobbyId")
                } else {
                    Log.e("LobbyViewModel", "Failed to join lobby: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("LobbyViewModel", "Error joining lobby: ${e.message}")
            }
        }
    }

    fun leaveLobby(lobbyId: String, playerId: UUID?) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.leaveLobby(lobbyId, playerId)
                if (response.isSuccessful) {
                    Log.d("LobbyViewModel", "Successfully leaved lobby $lobbyId")
                } else {
                    Log.e("LobbyViewModel", "Failed to leave lobby: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("LobbyViewModel", "Error leaving lobby: ${e.message}")
            }
        }
    }



    fun getPlayerList(): StateFlow<List<Player>> = players
    fun getLobbyMap(): StateFlow<Map<String, LobbyResponse>> = lobbies

    private fun PlayerResponse.toPlayer(): Player {
        return Player(id = this.id, name = this.name)
    }

}


