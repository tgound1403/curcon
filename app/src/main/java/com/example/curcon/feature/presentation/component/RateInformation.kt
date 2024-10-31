package com.example.curcon.feature.presentation.component

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.curcon.core.data.currency_rate.entity.CurrencyRateTrend
import com.example.curcon.core.data.currency_rate.entity.Trend
import com.example.curcon.core.extension.formatCurrency

@Composable
fun RateInformation(
    fromCurrency: String,
    indicativeRate: Double,
    toCurrency: String,
    someCountryRates: Map<String, Double>,
    currencyTrends: List<CurrencyRateTrend>
) {
    LaunchedEffect(currencyTrends) { Log.d("RateInfo", currencyTrends.toString()) }
    Column {
        Text(
            "Indicative Exchange Rate",
            style = TextStyle(fontSize = TextUnit(18f, TextUnitType.Sp), color = Color.Gray)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "1 $fromCurrency = ${indicativeRate.toString().formatCurrency()} $toCurrency",
            style = TextStyle(fontSize = TextUnit(20f, TextUnitType.Sp))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Others Currencies Exchange Rate",
            style = TextStyle(fontSize = TextUnit(18f, TextUnitType.Sp), color = Color.Gray)
        )
        Spacer(modifier = Modifier.height(8.dp))
        someCountryRates.forEach {
            Row {
                Text(
                    "1 $fromCurrency ~ ${it.value.toString().formatCurrency()} ${it.key}",
                    style = TextStyle(fontSize = TextUnit(20f, TextUnitType.Sp))
                )
                Spacer(modifier = Modifier.width(16.dp))
                for (item in currencyTrends) {
                    if (item.currencyCode == it.key) {
                        if (item.trend == Trend.UP) Icon(
                            Icons.Filled.KeyboardArrowUp,
                            "",
                            tint = Color.Green
                        ) else Icon(Icons.Filled.KeyboardArrowDown, "", tint = Color.Red)
                    }
                }
            }
        }
    }
}