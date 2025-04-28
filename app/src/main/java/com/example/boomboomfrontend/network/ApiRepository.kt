package com.example.boomboomfrontend.network

import com.example.boomboomfrontend.model.Player
import com.example.boomboomfrontend.model.PlayerResponse
import com.example.boomboomfrontend.network.RetrofitInstance.api
import retrofit2.Response

class ApiRepository {
    suspend fun addPlayer(player: Player) = RetrofitInstance.api.addPlayer(player)
    suspend fun getAllPlayers() = RetrofitInstance.api.getAllPlayers()
    suspend fun getPlayersInLobby(lobbyId: String): Response<List<PlayerResponse>> {
        return api.getPlayersInLobby(lobbyId)
    }

}
