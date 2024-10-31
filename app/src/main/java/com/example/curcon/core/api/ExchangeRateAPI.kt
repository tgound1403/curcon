package com.example.curcon.core.api

import com.example.curcon.BuildConfig
import com.example.curcon.core.api.model.ExchangeRateResult
import retrofit2.http.GET

private const val API_KEY = BuildConfig.API_KEY
interface ExchangeRateAPI {
    @GET("latest?access_key=$API_KEY")
    suspend fun check() : ExchangeRateResult
}