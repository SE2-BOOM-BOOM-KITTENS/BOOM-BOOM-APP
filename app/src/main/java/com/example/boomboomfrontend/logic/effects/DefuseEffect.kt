package com.example.boomboomfrontend.logic.effects

import com.example.boomboomfrontend.logic.CardEffect
import com.example.boomboomfrontend.logic.GameManager
import com.example.boomboomfrontend.model.Player
import com.example.boomboomfrontend.logic.CardManager

class DefuseEffect : CardEffect {
    override fun apply (player: Player, gameManager: GameManager){
        player.useDefuseCard()
        println ("${player.name} used a Defuse card!")
    }
}