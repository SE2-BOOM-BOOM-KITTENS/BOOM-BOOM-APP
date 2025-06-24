package com.example.boomboomfrontend.network.websocket

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.boomboomfrontend.network.messages.PlayerMessage
import com.example.boomboomfrontend.viewmodel.gameState.ClientInfoHolder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.sendText
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import org.json.JSONObject

val clientInfo = ClientInfoHolder.clientInfo
private val LOCAL_WEBSOCKET_URI = "ws://10.0.2.2:8080/game?id=${clientInfo.playerId}"
private val REMOTE_WEBSOCKET_URI = "ws://se2-demo.aau.at:53211/game?id=${clientInfo.playerId}"

class Stomp(private val callbacks: Callbacks) {


    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private lateinit var client: StompClient
    private var session: StompSession? = null

    private var sessionJob: Job? = null

    private var isConnected = false

    fun connect(onConnected: (()-> Unit)?=null) {
        if (isConnected) {
            callback("already connected")
            onConnected?.invoke()
            return
        }
        client = StompClient(OkHttpWebSocketClient())

        tryConnect( LOCAL_WEBSOCKET_URI,onConnected=onConnected){
            tryConnect(REMOTE_WEBSOCKET_URI, onConnected=onConnected)
        }
    }

    private fun tryConnect(uri: String, onConnected: (() -> Unit)?, fallback: (()->Unit)?=null){
        sessionJob = coroutineScope.launch {
            try{
                session = client.connect(uri)

                session?.let { stompSession ->
                    isConnected = true

                    launch {
                        stompSession.subscribeText("/topic/test").collectLatest { msg ->
                            handleIncomingMessage(msg)
                        }
                    }

                    launch {
                        stompSession.subscribeText("/topic/lobby/${clientInfo.currentLobbyID}").collectLatest { msg ->
                            handleIncomingMessage(msg)
                        }
                    }

                    launch {
                        stompSession.subscribeText("/user/queue/private").collectLatest { msg ->
                            handleIncomingMessage(msg)
                        }
                    }

                    callback("Websocket connected to $uri")
                    onConnected?.invoke()
                }
            } catch (e:Exception){
                Log.e("STOMP", "Connection error on $uri", e)
                callback("Connection error on $uri:${e.localizedMessage}")
                fallback?.invoke()
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
            put("payload", playerMessage.payload)
            put("lobbyId", playerMessage.lobbyId)
        }

        coroutineScope.launch {
            session?.sendText("/app/action", json.toString())
        }
    }

    //TO BE REMOVED; TEMPORARY
    fun joinGame(playerMessage: PlayerMessage, onJoined: (()-> Unit)?=null){
        val json = JSONObject().apply {
            put("payload",playerMessage.payload)
            put("playerName",playerMessage.playerName)
            put("action", null)
            put("lobbyId", playerMessage.lobbyId)
        }

        coroutineScope.launch {
            session?.sendText("/app/addPlayer",json.toString())
        }
        onJoined?.invoke()
    }

    fun sendErrorAction(playerMessage: PlayerMessage){
        val json = JSONObject().apply {
            put("playerName", playerMessage.playerName)
            put("action","ERROR")
            put("cardsPlayed", null)
            put("lobbyId", playerMessage.lobbyId)
        }

        coroutineScope.launch {
            session?.sendText("/app/action", json.toString())
        }
    }

    fun sendDebugTest(playerMessage: PlayerMessage){
        val json = JSONObject().apply {
            put("playerName", playerMessage.playerName)
            put("action","DEBUG")
            put("cardsPlayed", null)
            put("lobbyId", playerMessage.lobbyId)
        }

        coroutineScope.launch {
            session?.sendText("/app/test",json.toString())
        }
    }


    suspend fun disconnect() {
        coroutineScope.launch {
            try {
                session?.disconnect()
                session = null
                isConnected = false
                clientInfo.currentLobbyID = null
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
}
