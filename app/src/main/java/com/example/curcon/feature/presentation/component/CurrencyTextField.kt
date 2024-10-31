package com.example.curcon.feature.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyTextField(value: String, placeholder: String, onValueChanged: (String) -> Unit, enabled: Boolean = true) {
    TextField(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.LightGray.copy(.5f),
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        value = value,
        onValueChange = { onValueChanged.invoke(it) },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        placeholder = {
            Text(
                placeholder,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                color = Color.LightGray.copy(alpha = .9f)
            )
        },
        textStyle = TextStyle(fontSize = TextUnit(24f, TextUnitType.Sp), fontWeight = FontWeight.Medium, ),
        singleLine = true
    )
}