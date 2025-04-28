package com.example.boomboomfrontend.model
import com.example.boomboomfrontend.logic.CardManager
import com.example.boomboomfrontend.model.ConnectionStatus

data class Player(
    val id: String?,
    val name: String,
    var status: ConnectionStatus? = null,
    var defuseCount: Int = 1,
    var isAlive: Boolean = true,
    val hand: MutableList<Card> = mutableListOf()
) {
    fun useDefuseCard(): Boolean {
        return if (defuseCount > 0) {
            defuseCount--
            true
        } else {
            false
        }
    }

    fun hasDefuseCard(): Boolean {
        return defuseCount > 0
    }

    fun addDefuseCard() {
        defuseCount++
    }
    
     //Simuliert die Auswahl einer Position, wo eine Exploding Kitten-Karte ins Deck gelegt wird.
     //Aktuell über Konsole. Später wird das durch UI ersetzt.
    fun chooseDeckPosition(deckSize: Int): Int {
        println("Wähle eine Position für die Exploding Kitten Karte (0 = oben, $deckSize = unten):")
        return readLine()?.toIntOrNull()?.coerceIn(0, deckSize) ?: 0
    }
}
