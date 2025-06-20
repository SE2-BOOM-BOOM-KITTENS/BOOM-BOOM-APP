package com.example.boomboomfrontend.viewmodel.gameState

import android.util.Log
import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.CardType
import com.example.boomboomfrontend.model.Player
import org.json.JSONObject
import java.util.UUID

class GameStateRepository() {
    val clientInfo = ClientInfoHolder.clientInfo
    var players = mutableListOf<Player>()
    var cardHand: MutableList<Card> = mutableListOf()
    var playerCount: Int = 0
    var myTurn: Boolean = false

    fun processServerMessage(msg:String): Triple<String, String, JSONObject?>{
        Log.i("JSON_PROCESSING", "Processing json")
        val json = JSONObject(msg)
        Log.i("JSON_PROCESSING", "Processing type")
        val type = json.getString("type")
        Log.i("JSON_PROCESSING", "Processing message")
        val message = json.getString("message")
        Log.i("JSON_PROCESSING", "Processing payload")
        val gameStateJson = json.optJSONObject("payload")
        return Triple(type,message,gameStateJson)
    }

    fun updateState(gameStateJson: JSONObject?){
        Log.i("UPDATE_STATE",gameStateJson.toString())
        if(gameStateJson != null){
            clientInfo.currentLobbyID = UUID.fromString(gameStateJson.getString("lobbyId"))
            playerCount = gameStateJson.getInt("playerCount")
            //myTurn = gameStateJson.getBoolean("myTurn")

            val currentPlayer = gameStateJson.getJSONObject("currentPlayer")
            val id = UUID.fromString(currentPlayer.getString("id"))

            if(clientInfo.playerId == id){
                myTurn = true
            } else {
                myTurn = false
            }


            val playersJSON = gameStateJson.getJSONArray("players")
            for(i in 0 until playersJSON.length()){
                val playerJSON = playersJSON.getJSONObject(i)
                val player = Player(playerJSON.getString("id"),playerJSON.getString("name"))
                players.add(player)
            }
        }
    }

    fun updateHand(gameStateJson: JSONObject?){
        val handId = UUID.fromString(gameStateJson?.getString("playerId"))
        val newHand :MutableList<Card> = mutableListOf()
        val cardsJSON = gameStateJson?.getJSONArray("cards")

        for(i in 0 until cardsJSON!!.length()){
            val cardJSON = cardsJSON.getJSONObject(i)
            val type = cardJSON.getString("type")
            val card = Card(cardJSON.getString("name"), CardType.valueOf(type))
            newHand.add(card)
        }

        clientInfo.playerId = handId
        cardHand = newHand
        Log.i("HAND",gameStateJson.toString())

        if(checkForExplode()){
            //explode()
        }

    }

    fun checkForExplode(): Boolean{
        if(cardHand.isNotEmpty()){
            var explodingKitten = 0
            var defuse = 0

            for(card in cardHand){
                if(card.type == CardType.EXPLODING_KITTEN){
                    explodingKitten++
                }
                if(card.type == CardType.DEFUSE){
                    defuse++
                }
            }
            return explodingKitten > defuse
        }
        return false
    }

    fun getCardHandText(): List<String> {
        return cardHand.map { it.name }
    }
}