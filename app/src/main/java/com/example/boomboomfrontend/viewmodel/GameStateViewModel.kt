package com.example.boomboomfrontend.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.CardType
import com.example.boomboomfrontend.model.Player
import com.example.boomboomfrontend.network.messages.PlayerMessage
import com.example.boomboomfrontend.network.websocket.Callbacks
import com.example.boomboomfrontend.network.websocket.Stomp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import java.util.UUID

class GameStateViewModel :ViewModel() ,Callbacks {
    // fixme instead of passing this whole object just pass the callback method (after annotating the interface as functional)
    private val stomp = Stomp(this)

    lateinit var playerId: UUID
    var playerName: String

    lateinit var lobbyId: UUID
    var cardHand: MutableList<Card> = mutableListOf()

    private var playerCount: Int = 0
    val players: MutableList<Player> = mutableListOf()
    private var myTurn: Boolean = false
    private var cardType: String
    var gameOver: Boolean = false
    var gameReady: Boolean = false

    private val _responseMessage = MutableStateFlow("")
    val responseMessage: StateFlow<String> = _responseMessage

    init {
        playerName = "Steve"
        cardType = "BLANK\nCARD"
        cardHand = mutableListOf(Card("Nope", CardType.NOPE), Card("Defuse", CardType.DEFUSE), Card("Blank", CardType.BLANK), Card("Defuse", CardType.DEFUSE), Card("Cat Beard", CardType.CAT_BEARD))
        stomp.connect(playerName){
            joinGame()
        }
    }

    private fun processServerMessage(msg: String){
        try{
            val json = JSONObject(msg)

            val type = json.getString("type")
            val message = json.getString("message")
            val gameStateJson = json.optJSONObject("gameState")

            Log.i("JSON","[$type] $message")

            _responseMessage.value = message

            when(type){
                "GAME_STATE" -> updateState(gameStateJson)
                "HAND" -> updateHand(gameStateJson)
                "EXPLODE" -> handleExplosion()
            }


        } catch (e:Exception) {
            Log.e("GameStateError","Failed Updating GamesState: ${e.localizedMessage}")
        }
    }

    private fun updateState(gameStateJson: JSONObject?){
        Log.i("UPDATESTATE",gameStateJson.toString())
        if(gameStateJson != null){
            lobbyId = UUID.fromString(gameStateJson.getString("lobbyId"))
            playerCount = gameStateJson.getInt("playerCount")
            //myTurn = gameStateJson.getBoolean("myTurn")

            val currentPlayer = gameStateJson.getJSONObject("currentPlayer")
            val id = UUID.fromString(currentPlayer.getString("playerId"))

            if(playerId == id){
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

    private fun updateHand(gameStateJson: JSONObject?){
        val handId = UUID.fromString(gameStateJson?.getString("playerId"))
        val newHand :MutableList<Card> = mutableListOf()
        val cardsJSON = gameStateJson?.getJSONArray("cards")

        for(i in 0 until cardsJSON!!.length()){
            val cardJSON = cardsJSON.getJSONObject(i)
            val type = cardJSON.getString("type")
            val card = Card(cardJSON.getString("name"),CardType.valueOf(type))
            newHand.add(card)
        }

        playerId = handId
        cardHand = newHand
        Log.i("HAND",gameStateJson.toString())

        if(checkForExplode()){
            explode()
        }

    }

    private fun handleExplosion(){
    }

    override fun onResponse(res: String) {
        processServerMessage(res)
    }

    fun playCard(playerMessage: PlayerMessage){
        stomp.sendAction(playerMessage)
        stomp.getHand(playerName)
    }

    fun pass(playerMessage: PlayerMessage){
        stomp.sendAction(playerMessage)
        stomp.getHand(playerName)
    }

    private fun joinGame(){
        val playerMessage = PlayerMessage(playerName,null,null)
        stomp.joinGame(playerMessage)
    }

    private fun explode(){
        stomp.explode(playerName)
    }

    private fun checkForExplode():Boolean{
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
            return explodingKitten <= defuse
        }
        return false
    }

    fun getCardHandText(): List<String> {
        return cardHand.map { it.name }
    }
}