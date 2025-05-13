package com.example.boomboomfrontend.network.messages.networkPackets

import java.util.UUID

class PlayerNetworkPacket(
    val id:UUID,
    val name: String,
    val cardAmount: Int
) {
}