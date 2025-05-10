package com.example.boomboomfrontend.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GameScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CardUI()
            DeckUI()
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        PlayerHands()
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        OpponentHands()
    }
}

@Composable
fun CardUI() {
    Box(
        modifier = Modifier
            .size(110.dp, 150.dp)
            .background(Color(0xffb2766b))
    ) {
        Text(
            text = "BLANK\nCARD",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun DeckUI() {
    Box(
        modifier = Modifier
            .size(110.dp, 150.dp)
            .background(Color(0xff1c0e0b))
    ) {
        Text(
            text = "DECK",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun PlayerHands() {
    Box(
        modifier = Modifier
            .size(250.dp, 90.dp)
            .background(Color(0xffb2766b))
    ) {
        Text(
            text = "PLAYER\nHAND",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun OpponentHands() {
    Box(
        modifier = Modifier
            .size(250.dp, 90.dp)
            .background(Color(0xff1c0e0b))
    ) {
        Text(
            text = "OPPONENT\nHAND",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}