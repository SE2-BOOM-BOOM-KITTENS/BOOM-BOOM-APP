package com.example.boomboomfrontend

import com.example.boomboomfrontend.logic.CardManager
import com.example.boomboomfrontend.logic.effects.DefuseEffect
import com.example.boomboomfrontend.logic.GameManager
import com.example.boomboomfrontend.model.ConnectionStatus
import com.example.boomboomfrontend.model.Player
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DefuseEffectTest {

    @Test
    fun `defuse effect decreases count`(){
        val player = Player (id = "1", name = "Player", status = ConnectionStatus.JOINED, defuseCount = 0, isAlive = true)
        val effect = DefuseEffect()
        val dummyCardManager = CardManager()
        val dummyPlayerList = mutableListOf(player)

        val gameManager = GameManager(dummyCardManager, dummyPlayerList)

        effect.apply(player, gameManager)

        assertEquals(0, player.defuseCount)
    }

}