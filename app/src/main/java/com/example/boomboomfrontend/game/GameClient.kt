package com.example.boomboomfrontend.game

import org.hildan.krossbow.websocket.WebSocketClient

class GameClient {

    // WebSocket-Verbindung
    private lateinit var webSocketClient: WebSocketClient

    fun shuffleDeck(){
       // webSocketClient.sendMessage("SHUFFLE_DECK")
    }

    fun onMessageReceived(message: String){
        // verarbeite eingehende Nachrichten vom Server wie neuen Deck-Zustand
        if (message.contains("DECKSHUFFLED")){
            updateDeckState(message)
        }
    }

    private fun updateDeckState(message: String){
        // Logik um Deck-Zustand zu aktualisieren
    }

}