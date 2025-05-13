package com.example.boomboomfrontend.network.websocket

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.boomboomfrontend.network.messages.PlayerMessage
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

    fun connect(name: String, onConnected: (()-> Unit)?=null) {
        if (isConnected) {
            callback("already connected")
            onConnected?.invoke()
            return
        }
        client = StompClient(OkHttpWebSocketClient())

        sessionJob = coroutineScope.launch {
            try {
                session = client.connect(WEBSOCKET_URI + name)
           //     Log.e("TESST", WEBSOCKET_URI+name)

                session?.let { stompSession ->

                    launch {
                        Log.d("STOMP","Received on topic/test")
                        stompSession.subscribeText("/topic/test").collectLatest { msg ->
                            handleIncomingMessage(msg)
                        }
                    }

                    launch {
                            Log.d("STOMP","Received on user/queue/private")
                            stompSession.subscribeText("/user/queue/private").collectLatest {msg->
                                handleIncomingMessage(msg)
                            }
                        }


                    launch {
                        Log.d("STOMP","Received on topic/lobby/1234")
                        stompSession.subscribeText("/topic/lobby/1234").collectLatest { msg->
                            handleIncomingMessage(msg)
                        }
                    }

                    callback("WebSocket connected")
                    onConnected?.invoke()
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
            session?.sendText("/app/session", "Hello from client")
        }
    }

    fun sendAction(playerMessage: PlayerMessage) {
        val json = JSONObject().apply {
            put("playerName",playerMessage.playerName)
            put("action", playerMessage.action)
            put("payload", playerMessage.cardsPlayed)
        }

        coroutineScope.launch {
            session?.sendText("/app/action", json.toString())
        }
    }

    fun joinGame(playerMessage: PlayerMessage, onJoined: (()-> Unit)?=null){
        val json = JSONObject().apply {
            put("playerName",playerMessage.playerName)
            put("action", null)
            put("payload", null)
        }

        coroutineScope.launch {
            session?.sendText("/app/addPlayer",json.toString())
        }
        onJoined?.invoke()
    }

    fun getHand(playerName:String){
        val json = JSONObject().apply {
            put("playerName",playerName)
            put("action", null)
            put("payload", null)
        }

        coroutineScope.launch {
            session?.sendText("/app/getHand",json.toString())
        }
    }

    fun explode(playerName: String){
        val json = JSONObject().apply {
            put("playerName",playerName)
            put("action", "EXPLODE")
            put("payload", null)
        }

        coroutineScope.launch {
            session?.sendText("/app/action",json.toString())
        }
    }

    fun sendErrorAction(){

        val playerMessage = JSONObject().apply {
            put("playerName", "Steve")
            put("action","ERROR")
            put("cardsPlayed", null)
        }

        coroutineScope.launch {
            session?.sendText("/app/action", playerMessage.toString())
        }
    }

    fun sendDebugTest(){

        val playerMessage = JSONObject().apply {
            put("playerName", "Test")
            put("action","DEBUG")
            put("cardsPlayed", null)
        }

        coroutineScope.launch {
            session?.sendText("/app/test",playerMessage.toString())
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

    private fun handleIncomingMessage(msg: String){
        try{
            Log.i("HANDLEINCOMINGMESSSAGE", "Message is been handled")
            val json = JSONObject(msg)

            val type = json.getString("type")
            val message = json.getString("message")

            callback(msg)

        } catch (e:Exception){
            callback("Error parsing message: ${e.localizedMessage}")
        }
    }

    private fun handleIncomingMessage1(msg:String){
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
