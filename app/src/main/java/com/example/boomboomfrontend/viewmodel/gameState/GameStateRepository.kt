package com.example.boomboomfrontend.viewmodel.gameState

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.CardType
import com.example.boomboomfrontend.model.Player
import org.json.JSONObject
import java.util.UUID

class GameStateRepository() {
    val clientInfo = ClientInfoHolder.clientInfo
    var players = mutableListOf<Player>()
    var cardHand: MutableList<Card> = mutableListOf()
    var drawPile: MutableList<Card> = mutableListOf()
    var winner: Player? = null;
    var gameFinished by mutableStateOf(false)
    var playerCount: Int = 0
    var discardPile: MutableList<Card>? = null
    var cardPlayed: Card? = null
    var myTurn: Boolean = false
    var hasLastCardPlayed by mutableStateOf(false)

    fun processServerMessage(msg: String): Triple<String, String, JSONObject?> {
        Log.i("JSON_PROCESSING", "Processing json")
        val json = JSONObject(msg)
        Log.i("JSON_PROCESSING", "Processing type")
        val type = json.getString("type")
        Log.i("JSON_PROCESSING", "Processing message")
        val message = json.getString("message")
        Log.i("JSON_PROCESSING", "Processing payload")
        val gameStateJson = json.optJSONObject("payload")
        return Triple(type, message, gameStateJson)
    }

    fun updateState(gameStateJson: JSONObject?) {
        Log.i("UPDATE_STATE", gameStateJson.toString())
        if (gameStateJson != null) {
            players.clear()
            clientInfo.currentLobbyID = UUID.fromString(gameStateJson.getString("lobbyId"))
            playerCount = gameStateJson.getInt("playerCount")

            val currentPlayer = gameStateJson.getJSONObject("currentPlayer")
            val id = UUID.fromString(currentPlayer.getString("id"))

            if (clientInfo.playerId == id) {
                myTurn = true
            } else {
                myTurn = false
            }

            var discardPile = gameStateJson.getJSONObject("discardPile")
            val discardPileArray = discardPile.getJSONArray("cards")

            val discardPileList = mutableListOf<Card>()

            for (i in 0 until discardPileArray.length()) {
                val name = discardPileArray.getJSONObject(i).getString("name")
                val typeString = discardPileArray.getJSONObject(i).getString("type")
                val idString = discardPileArray.getJSONObject(i).getString("id")
                Log.i("ID","ID IS: $idString")
                val type = try{
                    CardType.valueOf(typeString.uppercase())
                } catch (e: IllegalArgumentException) {
                    throw IllegalArgumentException("Card type $typeString is not valid")
                }

                val cheatDuplicated = discardPileArray.getJSONObject(i).getBoolean("cheatDuplicated")

                val card = Card(name,type,cheatDuplicated,UUID.fromString(idString))
                discardPileList.add(card)
            }
            this.discardPile = discardPileList
            if(discardPileList.isNotEmpty()) {
                cardPlayed = discardPileList.last()
                hasLastCardPlayed = true
            }

            val winnerJson = gameStateJson.optJSONObject("winner")
            if (winnerJson != null) {
                winner = Player(
                    winnerJson.getString("id"),
                    winnerJson.getString("name")
                )
                gameFinished = true
            }
            val playersJSON = gameStateJson.getJSONArray("players")
            for (i in 0 until playersJSON.length()) {
                val playerJSON = playersJSON.getJSONObject(i)
                val player = Player(playerJSON.getString("id"), playerJSON.getString("name"))
                if (clientInfo.playerId != UUID.fromString(player.id)) {
                    players.add(player)
                }
            }
        }
    }

    fun updateHand(gameStateJson: JSONObject?){
        try {
            val handId = UUID.fromString(gameStateJson?.getString("playerId"))
            val newHand: MutableList<Card> = mutableListOf()
            val cardsJSON = gameStateJson?.getJSONArray("cards")

            for (i in 0 until cardsJSON!!.length()) {
                val cardJSON = cardsJSON.getJSONObject(i)
                val type = cardJSON.getString("type")
                val duplicated = cardJSON.getBoolean("cheatDuplicated")
                val id = cardJSON.getString("id")
                val card = Card(
                    cardJSON.getString("name"),
                    CardType.valueOf(type),
                    duplicated,
                    id = UUID.fromString(id)
                )
                newHand.add(card)
            }

            clientInfo.playerId = handId
            cardHand = newHand
            Log.i("HAND", gameStateJson.toString())

        }catch (e: Exception){
            Log.e("GameState","Failed to Update Hand ${e.localizedMessage}")
        }

    }

    fun checkForExplode(): Boolean {
        if (cardHand.isNotEmpty()) {
            var explodingKitten = 0
            var defuse = 0

            for (card in cardHand) {
                if (card.type == CardType.EXPLODING_KITTEN) {
                    explodingKitten++
                }
                if (card.type == CardType.DEFUSE) {
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

    var lastShuffleTimestamp by mutableStateOf<Long?>(null)

    fun notifyShuffle() {
        lastShuffleTimestamp = System.currentTimeMillis()
    }

    fun getCardCheatValue(): List<Boolean> {
        return cardHand.map { it.cheatDuplicated }

    }
}