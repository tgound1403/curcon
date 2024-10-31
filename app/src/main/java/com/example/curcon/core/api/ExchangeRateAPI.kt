package com.example.curcon.core.api

import com.example.curcon.core.api.model.ExchangeRateResult
import retrofit2.Response
import retrofit2.http.GET

private const val ACCESS_KEY = "27de7acc3bac036b46b2848fb457746e"

interface ExchangeRateAPI {

    @GET("latest?access_key=$ACCESS_KEY")
    suspend fun check() : ExchangeRateResult
    @GET
    suspend fun getLatestRate() : Response<Any>

    @GET
    suspend fun getHistorical() : Response<Any>

}