package com.example.boomboomfrontend.network

import com.example.boomboomfrontend.model.LobbyPlayer

data class CreateLobbyRequest(
    val player: LobbyPlayer,
    val maxPlayers: Int
)