package com.example.boomboomfrontend.ui.gameUI.dialogUI

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.boomboomfrontend.model.CardType
import com.example.boomboomfrontend.model.Card
import com.example.boomboomfrontend.ui.gameUI.border
import com.example.boomboomfrontend.ui.gameUI.cardfront
import kotlinx.coroutines.delay

@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun AlterTheFutureDialogUI(
    visible: Boolean = true,
    cards: List<Card> = mutableListOf(
        Card("Blank", CardType.BLANK),
        Card("Defuse", CardType.DEFUSE),
        Card("See the Future", CardType.SEE_THE_FUTURE),
        Card("See the Future", CardType.SEE_THE_FUTURE)
    ),
    onDismiss: () -> Unit = {},
    durationMillis: Long = 3000L
) {
    if (visible) {
        LaunchedEffect(Unit) {
            delay(durationMillis)
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
                Text("Choose your card order:", fontSize = 18.sp, color = Color.Black)

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    cards.forEach { card ->
                        Card(textField = card.name)
                    }
                }
            }
        }
    }
}
