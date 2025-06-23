package com.example.boomboomfrontend.model

data class Card(
    val name: String,
    val type: CardType,
    var aliasType: CardType? = null
)


