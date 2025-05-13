package com.example.boomboomfrontend.viewmodel

import androidx.lifecycle.ViewModel
import com.example.boomboomfrontend.network.messages.networkPackets.LobbyNetworkPacket

import com.example.boomboomfrontend.network.websocket.Callbacks
import com.example.boomboomfrontend.network.websocket.Stomp

class LobbyViewModel: ViewModel(), Callbacks {
    lateinit var lobbyState : LobbyNetworkPacket
    private val stomp = Stomp(this)

    override fun onResponse(res: String) {

    }
}