package com.example.boomboomfrontend.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.material3.Text


@Composable
fun ExitPopup(
    title: String = "Exit?",
    message: String = "Do you wish to disconnect",
    onPlay: () -> Unit,
    onPass: () -> Unit,
    onDismiss: () -> Unit
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            Button(onClick={
                onPlay()
                onDismiss()
            }){
                Text("Play")
            }
        },
        dismissButton = {
            Button(onClick = {
                onPass()
                onDismiss()
            }) {
                Text("Pass")
            }
        }
    )
}