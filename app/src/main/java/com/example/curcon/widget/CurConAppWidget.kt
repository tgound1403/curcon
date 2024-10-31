package com.example.curcon.widget

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.curcon.core.constant.AppConstant
import com.example.curcon.core.data.currency_rate.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurConAppWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Lấy database instance
        val database = AppDatabase.getDatabase(context)

        // Lấy rates từ database
        val rates = withContext(Dispatchers.IO) {
            val allRates = database.currencyRateDao().getAllRates()
            // Chỉ lấy rates của các đồng tiền trong famousCountries
            allRates.filter { it.currencyCode in AppConstant.famousCountries }
                .associate { it.currencyCode to it.rate }
        }

        provideContent {
            CurrencyDashboard(rates = rates)
        }
    }
}

@Composable
fun CurrencyDashboard(rates: Map<String, Double>) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header with base currency
        Text("Base Currency: EUR", style = TextStyle(fontSize = TextUnit(24f, TextUnitType.Sp)))
        Spacer(modifier = GlanceModifier.height(16.dp))
        // List of currency rates
        Column {
            for (item in rates) {
                CurrencyRateItem(item.key, item.value)
            }
        }

    }
}

@Composable
fun CurrencyRateItem(currency: String, rate: Double) {
    Row(
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currency,
            style = TextStyle(
                fontSize = TextUnit(16f, TextUnitType.Sp),
                fontWeight = FontWeight.Medium,
                fontFamily = poppinsFamily
            )
        )
        Spacer(modifier = GlanceModifier.width(48.dp))
        Text(
            text = String.format("%.2f", rate),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = TextUnit(20f, TextUnitType.Sp),
                fontFamily = poppinsFamily
            )
        )
    }
}

val poppinsFamily = FontFamily(
    family = "Poppins"
)