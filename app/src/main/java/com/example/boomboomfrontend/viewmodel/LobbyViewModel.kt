package com.example.boomboomfrontend.viewmodel

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boomboomfrontend.logic.Lobby
import com.example.boomboomfrontend.model.LobbyPlayer
import com.example.boomboomfrontend.model.Player
import com.example.boomboomfrontend.network.ApiRepository
import com.example.boomboomfrontend.network.CreateLobbyRequest
import com.example.boomboomfrontend.network.RetrofitInstance
import com.example.boomboomfrontend.network.messages.networkPackets.LobbyNetworkPacket

import com.example.boomboomfrontend.network.websocket.Callbacks
import com.example.boomboomfrontend.network.websocket.Stomp
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LobbyViewModel: ViewModel(), Callbacks {
    private val _lobbyState = MutableStateFlow<LobbyNetworkPacket?>(null)
    val lobbyState: StateFlow<LobbyNetworkPacket?> = _lobbyState
    private val stomp = Stomp(this)

    override fun onResponse(res: String) {

    }

    fun createLobby(player: LobbyPlayer, maxPlayers: Int = 4) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.createLobby(
                    CreateLobbyRequest(player, maxPlayers)
                )

                if (response.isSuccessful) {
                    _lobbyState.value = response.body()
                } else {
                    Log.e("LobbyError", "Code: ${response.code()}, Error: ${response.errorBody()?.string()}")
                    val json = Gson().toJson(CreateLobbyRequest(player, maxPlayers))
                    Log.d("CreateLobbyRequest_JSON", json)

                }
            } catch (e: Exception) {
                Log.e("Lobby Error", e.toString())

            }
        }
    }
}