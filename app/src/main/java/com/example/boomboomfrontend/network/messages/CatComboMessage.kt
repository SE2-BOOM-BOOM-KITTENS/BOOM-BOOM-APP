package com.example.boomboomfrontend.network.messages

import kotlinx.serialization.Serializable

@Serializable
data class CatComboMessage(
    val action: String,
    val cardIds: List<String>
)