package com.example.boomboomfrontend.ui.gameUI.dialogUI

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.boomboomfrontend.ui.gameUI.border
import com.example.boomboomfrontend.ui.gameUI.cardback
import com.example.boomboomfrontend.viewmodel.gameState.GameStateViewModel

@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun ExplodingKittenDialogUI(
    visible: Boolean = true,
    gameStateViewModel: GameStateViewModel = viewModel(),
    onDismiss: () -> Unit = {},
) {
    if (visible) {
        LaunchedEffect(Unit) {
            onDismiss()
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .zIndex(2f), // sits on top
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(16.dp)
                    .wrapContentHeight()
                    .widthIn(min = 200.dp, max = 400.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Game over, you got exploded!", fontSize = 18.sp, color = Color.Black)

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { gameStateViewModel.exit() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(cardback)
                    ),
                    modifier = Modifier
                        .size(120.dp, 40.dp)
                        .border(2.dp, Color(border), RoundedCornerShape(10.dp))
                        .background(Color(cardback), RoundedCornerShape(10.dp)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Exit Game",
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
