package com.example.boomboomfrontend.network

import com.example.boomboomfrontend.model.LobbyPlayer
import com.example.boomboomfrontend.model.Player
import com.example.boomboomfrontend.model.PlayerResponse
import com.example.boomboomfrontend.network.RetrofitInstance.api
import com.example.boomboomfrontend.network.messages.networkPackets.LobbyNetworkPacket
import retrofit2.Response
import java.util.UUID

// fixme rename to show its handling players for maintainability
class ApiRepository {

    suspend fun addPlayer(string: String) = RetrofitInstance.api.addPlayer(string)
    suspend fun getAllPlayers() = RetrofitInstance.api.getAllPlayers()
    suspend fun getPlayersInLobby(lobbyId: String): Response<List<PlayerResponse>> {
        return api.getPlayersInLobby(lobbyId)
    }
    suspend fun createLobby(creator: Player, maxPlayers: Int): Response<LobbyNetworkPacket> {
        val request = CreateLobbyRequest(UUID.fromString(creator.id) , maxPlayers)
        return api.createLobby(request)
    }

}
