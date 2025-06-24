package com.example.boomboomfrontend.viewmodel.lobby

import android.util.Log
import com.example.boomboomfrontend.network.websocket.Callbacks
import com.example.boomboomfrontend.network.websocket.Stomp
import org.json.JSONObject

class StompLobbyService(callbacks: Callbacks) {

    init {
        Stomp.setCallbacks(callbacks)
    }

    fun connect(onConnect: ()->Unit){
        Stomp.connect(onConnect)
    }

    fun createGame(){
        Stomp.createGame()
    }

    fun processServerMessage(msg:String): String {
        Log.i("JSON_PROCESSING", "Processing json")
        val json = JSONObject(msg)
        Log.i("JSON_PROCESSING", "Processing type")
        val type = json.getString("type")
        return type
    }
}