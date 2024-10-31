package com.example.curcon.feature.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.curcon.core.extension.formatCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeConverter(
    amount: Double,
    convertedAmount: Double,
    onAmountChanged: (String) -> Unit,
    onChanged: () -> Unit,
    currencies: List<String>,
    fromCurrency: String,
    toCurrency: String,
    onFromCurrencyChanged: (String) -> Unit,
    onToCurrencyChanged: (String) -> Unit,
    onSwap: () -> Unit
) {
    // Add these states to track the animation
    var animateSwap by remember { mutableStateOf(false) }
    val offsetAnimation by animateFloatAsState(
        targetValue = if (animateSwap) 2.05f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .border(0.dp, Color.Transparent, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(16.dp))
                .padding(32.dp)
        ) {
            // First TextField Section
            Text("Amount")
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // First TextField content
                    CurrencyDropdownMenu(
                        currencies = currencies,
                        selectedCurrency = fromCurrency,
                        onCurrencySelected = onFromCurrencyChanged,
                        modifier = Modifier.offset(y = (offsetAnimation * 100).dp),
                    )
                    Spacer(Modifier.width(16.dp))
                    CurrencyTextField(
                        if (amount != 0.0) amount.toString() else "",
                        onValueChanged = {
                            if (it.isNotEmpty()) {
                                onAmountChanged(it)
                            } else {
                                onAmountChanged("0")
                            }
                            onChanged()
                        },
                        placeholder = "Input amount to convert",
                    )
                }
            }

            // Swap Button Section
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(vertical = 32.dp)
            ) {
                HorizontalDivider()
                Column(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .padding(vertical = 8.dp, horizontal = 24.dp)
                        .rotate(90f)
                        .clickable {
                            animateSwap = !animateSwap
                            onSwap.invoke()
                        },
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Filled.ArrowBack, "")
                    Icon(Icons.Filled.ArrowForward, "")
                }
            }

            // Second TextField Section
            Text("Converted Amount")
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Second TextField content
                    CurrencyDropdownMenu(
                        currencies = currencies,
                        selectedCurrency = toCurrency,
                        onCurrencySelected = onToCurrencyChanged,
                        modifier = Modifier.offset(y = (-offsetAnimation * 100).dp),
                    )
                    Spacer(Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(color = Color.LightGray.copy(alpha = .5f))
                            .padding(all = 16.dp)
                            .width(200.dp)
                            .height(24.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            if (convertedAmount != 0.0) convertedAmount.toString()
                                .formatCurrency() else "",
                            style = TextStyle(
                                fontSize = TextUnit(24f, TextUnitType.Sp),
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}
