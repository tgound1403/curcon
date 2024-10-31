package com.example.curcon.feature.data.source.remote

import com.example.curcon.core.api.model.ApiService
import com.example.curcon.core.api.model.ApiService.Companion.moshi
import com.example.curcon.core.api.model.ExchangeRateResult
import com.squareup.moshi.Json
import retrofit2.Response

interface ExchangeRateRemoteDataSource {
    suspend fun getExchangeRates(): ExchangeRateResult?
}

class ExchangeRateRemoteDataSourceImpl : ExchangeRateRemoteDataSource {
    override suspend fun getExchangeRates(): ExchangeRateResult? {
        return ApiService.exchangeRateAPI.check()
    }
}