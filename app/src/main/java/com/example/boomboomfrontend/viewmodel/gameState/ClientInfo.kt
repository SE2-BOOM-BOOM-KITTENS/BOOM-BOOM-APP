package com.example.boomboomfrontend.viewmodel.gameState

import java.util.UUID

object ClientInfoHolder {
    var clientInfo: ClientInfo = ClientInfo()
}

data class ClientInfo(
    var playerId: UUID? = UUID.fromString("00000000-0000-0000-0000-000000001234"),
    var playerName: String = "Steve",
    var currentLobbyID: UUID? = UUID.fromString("00000000-0000-0000-0000-000000001234")
)