package com.example.boomboomfrontend

import com.example.boomboomfrontend.model.ConnectionStatus
import com.example.boomboomfrontend.model.Player
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PlayerTest {

    @Test
    fun `player starts alive and has defuse`(){
        val player = Player (id = 1.toString(), name = "Player1", status = ConnectionStatus.JOINED ,defuseCount = 1, isAlive = true)

        val hasCard = player.hasDefuseCard()

        assertTrue (player.isAlive)
        assertTrue (hasCard)
        assertEquals (1, player.defuseCount)
    }

    @Test
    fun `useDefuseCard decreases count`(){
        val player = Player (id = 2.toString(), name = "Player2", status = ConnectionStatus.JOINED , defuseCount = 2, isAlive = true)

        val used = player.useDefuseCard()

        assertTrue (used)
        assertEquals (1, player.defuseCount)
    }

    @Test
    fun `useDefuseCard fails when none available`(){
        val player = Player (id = 3.toString(), name = "Player3", status = ConnectionStatus.JOINED ,defuseCount = 0, isAlive = true)

        val used = player.useDefuseCard()

        assertFalse (used)
        assertFalse (player.hasDefuseCard())
        assertEquals (0, player.defuseCount)
    }

    @Test
    fun `hasDefuseCard fails when none available`(){
        val player = Player (id = 4.toString(), name = "Player4", status = ConnectionStatus.JOINED  ,defuseCount = 0, isAlive = true)

        val hasCard = player.hasDefuseCard()

        assertFalse (hasCard)
        assertEquals (0, player.defuseCount)
    }

    @Test
    fun `addDefuseCard increases count`(){
        val player = Player (id = 5.toString(), name = "Player5", status = ConnectionStatus.JOINED , defuseCount = 0, isAlive = true)

        player.addDefuseCard()

        assertEquals (1, player.defuseCount)
    }


}