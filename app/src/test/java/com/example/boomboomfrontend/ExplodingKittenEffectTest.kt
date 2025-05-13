//package com.example.boomboomfrontend
//
//import com.example.boomboomfrontend.logic.CardManager
//import com.example.boomboomfrontend.logic.effects.ExplodingKittenEffect
//import com.example.boomboomfrontend.logic.GameManager
//import com.example.boomboomfrontend.model.ConnectionStatus
//import com.example.boomboomfrontend.model.Player
//import org.junit.jupiter.api.Assertions.*
//import org.junit.jupiter.api.Test
//
//class ExplodingKittenEffectTest {
//
//    @Test
//    fun `exploding kitten kills player without defuse`(){
//        val player = Player (id = "1", name = "Player1", status = ConnectionStatus.JOINED, defuseCount = 0, isAlive = true)
//        val effect = ExplodingKittenEffect()
//        val dummyCardManager = CardManager()
//        val dummyPlayerList = mutableListOf(player)
//
//        val gameManager = GameManager(dummyCardManager, dummyPlayerList)
//
//        effect.apply(player, gameManager)
//
//        assertFalse(player.isAlive)
//    }
//
//    @Test
//    fun `exploding kitten defused when player has defuse card`(){
//        val player = Player (id = "2", name = "Player2", status = ConnectionStatus.JOINED, defuseCount = 1, isAlive = true)
//        val effect = ExplodingKittenEffect()
//        val dummyCardManager = CardManager()
//        val dummyPlayerList = mutableListOf(player)
//
//        val gameManager = GameManager(dummyCardManager, dummyPlayerList)
//
//        effect.apply(player, gameManager)
//
//        assertTrue(player.isAlive)
//        assertEquals(0, player.defuseCount)
//    }
//
//}