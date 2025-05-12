package com.example.boomboomfrontend.network.websocket

import android.os.Handler
import android.os.Looper
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.sendText
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import org.json.JSONObject

private const val WEBSOCKET_URI = "ws://10.0.2.2:8080/game?name="

class Stomp(private val callbacks: Callbacks) {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private lateinit var client: StompClient
    private var session: StompSession? = null

    private var sessionJob: Job? = null

    private var isConnected = false

    private var playerName = ""

    fun connect(name: String) {
        playerName = name

        if (isConnected) {
            callback("already connected")
            return
        }
        client = StompClient(OkHttpWebSocketClient())


        sessionJob = coroutineScope.launch {
            try {
                session = client.connect(WEBSOCKET_URI + name)
           //     Log.e("TESST", WEBSOCKET_URI+name)

                session?.let { stompSession ->
                    launch {
                        stompSession.subscribeText("/topic/test").collectLatest { msg ->
                            try {
                                // if Msg is JSON
                                if (msg.trim().startsWith("{")) {
                                    val json = JSONObject(msg)
                                    val from = json.optString("from", "Server")
                                    val text = json.optString("text", msg)
                                    callback("[$from] $text")
                                } else {
                                    // WebSocket works!
                                    callback(msg)
                                }
                            } catch (e: Exception) {
                                callback("Error: $msg")
                            }
                        }
                    }

                    callback("WebSocket connected")
                }
            } catch (e: Exception) {
                Log.e("STOMP", "Connection error", e)
                callback("Connection Error: ${e.localizedMessage}")
            }
        }
    }

    fun sendResponseMessage() {
        coroutineScope.launch {
            if (session == null) {
                callback("Not Connected yet")
                return@launch
            }

            val json = JSONObject().apply {
                put("playerName", playerName)
                put("action", "TEST")
                put("cardsPlayed", org.json.JSONArray()) // leeres Array
            }

            Log.e("STOMP", "Sende TEST-Message an /app/test: $json")
            session?.sendText("/app/test", json.toString())
        }
    }




    fun sendAction(action: String, payload: String, playerName: String = "Andi") {
        val json = JSONObject().apply {
            put("playerName", playerName)
            put("action", action)
            put("cardsPlayed", org.json.JSONArray())
        }

        coroutineScope.launch {
            session?.sendText("/app/action", json.toString())
        }
    }



    fun disconnect() {
        coroutineScope.launch {
            try {
                session?.disconnect()
                callback("Disconnected")
            } catch (e: Exception) {
                callback("Error disconnecting: ${e.localizedMessage}")
            } finally {
                coroutineScope.cancel()
            }
        }
    }

    private fun callback(msg: String) {
        Handler(Looper.getMainLooper()).post {
            callbacks.onResponse(msg)
        }
    }
}
