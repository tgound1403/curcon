package com.example.curcon.feature.data.source.local

import android.app.Application
import com.example.curcon.core.data.currency_rate.AppDatabase
import com.example.curcon.core.data.currency_rate.entity.CurrencyRate
import javax.inject.Inject

class ExchangeRateLocalDataSource @Inject constructor(private val application: Application) {
    suspend fun getCurrencyRate() : List<CurrencyRate> {
        return AppDatabase.getDatabase(application).currencyRateDao().getAllRates()
    }

    suspend fun getLatestRateForCurrency(currencyCode: String) : CurrencyRate {
        return AppDatabase.getDatabase(application).currencyRateDao().getRateForCurrency(currencyCode)!!
    }

    suspend fun saveNewRates(rates: List<CurrencyRate>) {
        AppDatabase.getDatabase(application).currencyRateDao().insertRates(rates)
    }
}