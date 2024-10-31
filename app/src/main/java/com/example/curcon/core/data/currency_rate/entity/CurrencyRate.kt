package com.example.curcon.core.data.currency_rate.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "currency_rates")
data class CurrencyRate(
    @PrimaryKey
    val currencyCode: String,
    val rate: Double,
)

class RatesTypeConverter {
    @TypeConverter
    fun fromRatesMap(rates: Map<String, Double>): String {
        return Gson().toJson(rates)
    }

    @TypeConverter
    fun toRatesMap(ratesString: String): Map<String, Double> {
        val type = object : TypeToken<Map<String, Double>>() {}.type
        return Gson().fromJson(ratesString, type)
    }
}
