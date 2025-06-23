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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
        Card("See the Future", CardType.SEE_THE_FUTURE)
    ),
    onOrderConfirmed: (List<Card>) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    if (!visible) return

    val selectedIndices = remember { mutableStateListOf<Int>() }

    LaunchedEffect(selectedIndices.size) {
        if (selectedIndices.size == 3) {
            val reordered = selectedIndices.map { cards[it] }
            delay(1000) // Optional: Give user feedback before closing
            onOrderConfirmed(reordered)
            onDismiss()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .zIndex(2f),
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
            Text("Reorder the top 3 cards", fontSize = 18.sp, color = Color.Black)

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                cards.forEachIndexed { index, card ->
                    val isSelected = selectedIndices.contains(index)
                    val selectionOrder = selectedIndices.indexOf(index) + 1

                    CardSelectable(
                        card = card,
                        selectedOrder = if (isSelected) selectionOrder else null,
                        onClick = {
                            if (!isSelected && selectedIndices.size < 3) {
                                selectedIndices.add(index)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CardSelectable(card: Card, selectedOrder: Int?, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(110.dp, 150.dp)
            .background(Color(cardfront), RoundedCornerShape(10.dp))
            .border(2.dp, Color(border), RoundedCornerShape(10.dp))
            .clickable { onClick() })
    {
        Text(
            text = card.name,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
        selectedOrder?.let {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .background(Color.Red, RoundedCornerShape(10.dp))
                    .size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("$it", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}
