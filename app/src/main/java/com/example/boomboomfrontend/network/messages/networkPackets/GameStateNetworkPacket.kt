package com.example.boomboomfrontend.network.messages.networkPackets

import java.util.UUID

data class GameStateNetworkPacket(
    val lobbyId : UUID,
    val players: List<PlayerNetworkPacket>

) {

}