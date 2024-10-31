package com.example.curcon.feature.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.example.curcon.ui.theme.poppinsFamily
import kotlinx.coroutines.delay
import java.text.BreakIterator
import java.text.StringCharacterIterator

@Composable
fun AnimatedText() {
    val text = "CURCON"
    val desc = "Check live rates, fast, and easy."
    val breakIterator = remember(text) { BreakIterator.getCharacterInstance() }
    val typingDelayInMs = 50L
    var substringText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        delay(600)
        breakIterator.text = StringCharacterIterator(text)
        var nextIndex = breakIterator.next()
        while (nextIndex != BreakIterator.DONE) {
            substringText = text.subSequence(0, nextIndex).toString()
            nextIndex = breakIterator.next()
            delay(typingDelayInMs)
        }
    }

    Text(
        substringText,
        style = TextStyle(
            fontSize = TextUnit(64f, TextUnitType.Sp),
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily
        )
    )

    Text(
        modifier = Modifier.fillMaxWidth(.8f),
        text = desc,
        style = TextStyle(
            textAlign = TextAlign.Center,
            fontSize = TextUnit(14f, TextUnitType.Sp),
            color = Color.Gray,
            fontWeight = FontWeight.Normal,
            fontFamily = poppinsFamily
        )
    )
}
