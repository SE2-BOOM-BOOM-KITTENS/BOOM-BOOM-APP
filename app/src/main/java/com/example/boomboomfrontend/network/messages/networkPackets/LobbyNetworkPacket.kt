package com.example.boomboomfrontend.network.messages.networkPackets

import com.example.boomboomfrontend.model.Player
import java.util.UUID

data class LobbyNetworkPacket(
    val lobbyId: UUID,
    val creator: Player,
    val players: List<Player>,
    val maxPlayers: Int
)