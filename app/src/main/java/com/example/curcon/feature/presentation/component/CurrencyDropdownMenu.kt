package com.example.curcon.feature.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curcon.ui.theme.poppinsFamily

@Composable
fun CurrencyDropdownMenu(
    currencies: List<String>,
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
    modifier: Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredCurrencies = remember(searchQuery, currencies) {
        if (searchQuery.isEmpty()) currencies
        else currencies.filter { it.contains(searchQuery, ignoreCase = true) }
    }
    Box {
        Row(
            modifier
                .padding(top = 8.dp, bottom = 8.dp)
                .clickable { expanded = true },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedCurrency, color = Color.Blue, style = TextStyle(
                    fontSize = 28.sp, fontFamily = poppinsFamily, fontWeight = FontWeight.Medium
                ), modifier = Modifier.fillMaxWidth(0.3f)
            )
            ExpandIndicator(expanded)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 300.dp)
        ) {
            Column {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    placeholder = { Text("Search currency") },
                    singleLine = true
                )


                filteredCurrencies.forEach { currency ->
                    DropdownMenuItem(onClick = {
                        onCurrencySelected(currency)
                        expanded = false
                        searchQuery = ""
                    }, text = { Text(currency, style = TextStyle(color = Color.Black)) })
                }

            }
        }
    }
}
