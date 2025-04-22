package com.example.boomboomfrontend.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.boomboomfrontend.R
import com.example.boomboomfrontend.logic.CardManager
import com.example.boomboomfrontend.logic.GameManager
import com.example.boomboomfrontend.model.Player
import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.model.CardType
import com.example.boomboomfrontend.viewmodel.PlayerViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GameActivity : ComponentActivity() {

    private val playerViewModel: PlayerViewModel by viewModels()
    private lateinit var drawButton: Button
    private lateinit var statusText: TextView
    private lateinit var gameManager: GameManager
    private lateinit var cardManager: CardManager

    private var localPlayers: MutableList<Player> = mutableListOf()
    private var lobbyId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        statusText = findViewById(R.id.statusText)
        drawButton = findViewById(R.id.drawButton)

        // Hole lobbyId aus Intent
        lobbyId = intent.getStringExtra("lobbyId")

        // Initialisiere Logik-Komponenten
        cardManager = CardManager()

        // Lade Spieler aus Lobby
        if (lobbyId != null) {
            playerViewModel.getPlayersInLobby(lobbyId!!)
        }

        // Beobachte REST-Ergebnis
        lifecycleScope.launch {
            playerViewModel.players.collectLatest { playerResponses ->
                if (playerResponses.isNotEmpty()) {
                    // Konvertiere PlayerResponse zu Player-Objekten
                    localPlayers = playerResponses.map {
                        Player(name = it.name, id = it.id, isAlive = true)
                    }.toMutableList()

                    // Deck und GameManager initialisieren
                    cardManager.initializeDeck(localPlayers)
                    localPlayers.forEach {
                        it.hand.clear() // optional, falls wir die Hand neu aufbauen wollen
                        it.hand.addAll(List(5) { cardManager.drawCard()!! })
                    }
                    gameManager = GameManager(cardManager, localPlayers)

                    updateUI()
                }
            }
        }

        // Button-Aktion
        drawButton.setOnClickListener {
            if (::gameManager.isInitialized) {
                gameManager.endTurn()
                gameManager.currentPlayerIndex =
                    (gameManager.currentPlayerIndex + 1) % localPlayers.size
                updateUI()
            }
        }
    }

    private fun updateUI() {
        val currentPlayer = gameManager.playerByIndex()
        statusText.text = "Am Zug: ${currentPlayer.name} | Karten: ${currentPlayer.hand.size}"
    }
}