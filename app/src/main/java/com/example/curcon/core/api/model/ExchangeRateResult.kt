package com.example.curcon.core.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExchangeRateResult (
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)