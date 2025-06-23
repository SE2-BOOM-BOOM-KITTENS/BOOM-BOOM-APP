package com.example.boomboomfrontend.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun WinPopup(
    title: String = "Winner!",
    message: String = "Congratulation, you won!",
    onPlay: () -> Unit,
    onDismiss: () -> Unit
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            Button(onClick={
                onPlay()
            }){
                Text("Close")
            }
        }
    )
}