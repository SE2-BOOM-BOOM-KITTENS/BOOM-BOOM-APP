package com.example.boomboomfrontend.network


import java.util.UUID

data class CreateLobbyRequest(
    val playerId: UUID,
    val maxPlayers: Int
)