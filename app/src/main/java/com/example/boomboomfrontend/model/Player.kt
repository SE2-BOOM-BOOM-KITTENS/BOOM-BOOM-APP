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
}

