package com.example.curcon.feature.domain.repositories

import com.example.curcon.core.api.model.ExchangeRateResult
import com.example.curcon.core.data.currency_rate.entity.CurrencyRate
import com.example.curcon.feature.data.source.local.ExchangeRateLocalDataSource
import com.example.curcon.feature.data.source.remote.ExchangeRateRemoteDataSource

// CurrencyRepository.kt
interface ExchangeRateRepository {
    suspend fun getExchangeRates(): Map<String, Double>

    suspend fun getExchangeRateAPIResponse(): ExchangeRateResult

    suspend fun getAllCurrencyRateDataFromLocal(): List<CurrencyRate>

    suspend fun getAllExchangeRateDataFromLocal(): Map<String, Double>

    suspend fun getLatestRateOfCurrency(currencyCode: String): CurrencyRate

    suspend fun saveNewRatesToLocal(rates: List<CurrencyRate>)
}

class ExchangeRateRepositoryImpl(
    private val remoteDataSource: ExchangeRateRemoteDataSource,
    private val localDataSource: ExchangeRateLocalDataSource
) :
    ExchangeRateRepository {
    override suspend fun getExchangeRates(): Map<String, Double> {
        return remoteDataSource.getExchangeRates()?.rates ?: mapOf()
    }

    override suspend fun getExchangeRateAPIResponse(): ExchangeRateResult {
        return remoteDataSource.getExchangeRates()!!
    }

    override suspend fun getAllCurrencyRateDataFromLocal(): List<CurrencyRate> {
        return localDataSource.getCurrencyRate()
    }

    override suspend fun getAllExchangeRateDataFromLocal(): Map<String, Double> {
        val localData = getAllCurrencyRateDataFromLocal()
        val rates = mutableMapOf<String, Double>()
        for (item in localData) {
            rates[item.currencyCode] = item.rate
        }
        return rates
    }

    override suspend fun getLatestRateOfCurrency(currencyCode: String): CurrencyRate {
        return localDataSource.getLatestRateForCurrency(currencyCode)
    }

    override suspend fun saveNewRatesToLocal(rates: List<CurrencyRate>) {
        localDataSource.saveNewRates(rates)
    }
}
