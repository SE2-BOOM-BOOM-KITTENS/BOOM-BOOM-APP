package com.example.boomboomfrontend.network.messages

import com.example.boomboomfrontend.viewmodel.gameState.ClientInfoHolder
import java.util.UUID

val clientInfo = ClientInfoHolder.clientInfo

data class PlayerMessage (
    val playerName: String? = clientInfo.playerName,
    val action: String? = "",
    val payload: Any? = null,
    val targetId: String? = null,
    val lobbyId: UUID? = clientInfo.currentLobbyID
)