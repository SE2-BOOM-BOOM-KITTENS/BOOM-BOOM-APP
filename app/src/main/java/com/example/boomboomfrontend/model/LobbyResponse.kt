package com.example.boomboomfrontend.model

data class LobbyResponse(
    val id: String,
    val creator: Player,
    val players: List<Player>,
    val maxPlayers: Int
)
