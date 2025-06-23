package com.example.boomboomfrontend.logic

import com.example.boomboomfrontend.model.Player
import com.example.boomboomfrontend.model.ConnectionStatus.*
import kotlinx.coroutines.*
import kotlin.random.Random

// Lobby Klasse
class Lobby(private val host: Player, private val maxPlayers: Int) {
    private val players = mutableListOf(host)
    private val roomCode = Random.nextInt(1000, 9999).toString()
    private var countdownJob: Job? = null

    // Neuer Scope für die Lobby, an Lebenszyklus koppelbar
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun getRoomCode(): String = roomCode

//    fun joinLobby(player: Player): Boolean {
//        if (players.size >= maxPlayers) {
//            println("${player.name} konnte nicht beitreten. Raum ist voll!")
//            return false
//        }
//        player.status = CONNECTION_PENDING
//        players.add(player)
//        println("${player.name} ist beigetreten. Status: ${player.status}")
//        simulateConnection(player)
//        return true
//    }

    private fun simulateConnection(player: Player) {
        scope.launch {
            delay(Random.nextLong(1000, 3000)) // Simulierte Verbindungszeit
            player.status = JOINED
            println("${player.name} ist jetzt verbunden!")
            checkAndStartGame()
        }
    }

    private fun checkAndStartGame() {
        if (players.all { it.status == JOINED }) {
            startCountdown()
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = scope.launch {
            for (i in 5 downTo 1) {
                println("Spiel startet in $i Sekunden...")
                delay(1000)
            }
            startGame()
        }
    }

    private fun startGame() {
        println("Spiel startet jetzt mit ${players.size} Spielern!")
    }

    // Optionale Aufräumfunktion, z. B. wenn Lobby gelöscht wird
    fun cancelLobby() {
        scope.cancel()
    }
}

fun main() {
    val host = Player("0", "Host", JOINED, 1, true)
    val lobby = Lobby(host, 4)
    println("Lobby erstellt! Code: ${lobby.getRoomCode()}")

    val players = listOf(
        Player("1", "Spieler1", NOT_CONNECTED, 1, true),
        Player("2", "Spieler2", NOT_CONNECTED, 1, true),
        Player("3", "Spieler3", NOT_CONNECTED, 1, true)
    )

//    players.forEach { lobby.joinLobby(it) }

    Thread.sleep(10000) // Hauptthread läuft, damit die Coroutines Zeit haben
    lobby.cancelLobby() // Aufräumen
}