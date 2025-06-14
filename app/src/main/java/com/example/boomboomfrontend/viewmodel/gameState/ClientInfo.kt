package com.example.boomboomfrontend.viewmodel.gameState

import java.util.UUID

data class ClientInfo(
    var playerId: UUID? = null,
    var playerName: String = "Steve",
    var currentLobbyID: UUID? = null
)