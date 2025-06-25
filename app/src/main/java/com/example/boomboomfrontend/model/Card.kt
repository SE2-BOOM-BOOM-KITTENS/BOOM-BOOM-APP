package com.example.boomboomfrontend.model

import java.util.UUID

data class Card(
    val name: String,
    val type: CardType,
    val cheatDuplicated: Boolean = false,
    val id: UUID? = null
)


