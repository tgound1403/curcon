package com.example.curcon.core.data.currency_rate.entity

data class CurrencyRateTrend(
    val currencyCode: String,
    val currentRate: Double,
    val previousRate: Double,
    val trend: Trend
)

enum class Trend {
    UP,
    DOWN,
    UNCHANGED
}
