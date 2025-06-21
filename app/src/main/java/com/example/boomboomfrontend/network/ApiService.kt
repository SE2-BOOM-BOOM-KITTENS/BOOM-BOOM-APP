package com.example.boomboomfrontend.network

import com.example.boomboomfrontend.model.PlayerResponse
import com.example.boomboomfrontend.network.messages.networkPackets.LobbyNetworkPacket
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

// fixme add player information in name
interface ApiService {
    @POST("/players")
    suspend fun addPlayer(@Body name: String): Response<ResponseBody>

    @GET("/players/allPlayers")
    suspend fun getAllPlayers(): Response<List<PlayerResponse>>

    @GET("lobbies/players")
    suspend fun getPlayersInLobby(
        @Header("lobbyId") lobbyId: String
    ): Response<List<PlayerResponse>>

    @POST("lobbies")
    suspend fun createLobby(@Body request: CreateLobbyRequest): Response<LobbyNetworkPacket>

}
