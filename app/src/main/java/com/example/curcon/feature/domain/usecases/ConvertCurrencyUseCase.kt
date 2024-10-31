package com.example.curcon.feature.domain.usecases

import com.example.curcon.core.api.model.ExchangeRateResult
import com.example.curcon.core.data.currency_rate.entity.CurrencyRate
import com.example.curcon.feature.domain.repositories.ExchangeRateRepository

class ConvertCurrencyUseCase(private val repository: ExchangeRateRepository) {
    suspend fun convert(amount: Double, fromCurrency: String, toCurrency: String): Double {
        val rates = repository.getAllExchangeRateDataFromLocal()
        val fromRate = rates[fromCurrency] ?: 1.0
        val toRate = rates[toCurrency] ?: 1.0
        return amount * (toRate / fromRate)
    }
}

class GetCurrencyRatesUseCase(private val repository: ExchangeRateRepository) {
    suspend fun getSupportCurrencies() : List<String> {
        val rates = repository.getExchangeRates()

        return rates.keys.toList().sorted()
    }

    suspend fun getNewRateExchangeAPIResponse() : ExchangeRateResult {
        return repository.getExchangeRateAPIResponse()
    }

    suspend fun getAllExchangeRateFromLocal() : Map<String, Double> {
        return repository.getAllExchangeRateDataFromLocal()
    }

    suspend fun getAllCurrencyRateFromLocal() : List<CurrencyRate> {
        return repository.getAllCurrencyRateDataFromLocal()
    }

    suspend fun getCurrencyRateFromLocal(currencyCode: String) : CurrencyRate {
        return repository.getLatestRateOfCurrency(currencyCode)
    }

    suspend fun saveNewRatesToLocal(rates: List<CurrencyRate>) {
        repository.saveNewRatesToLocal(rates)
    }
}
