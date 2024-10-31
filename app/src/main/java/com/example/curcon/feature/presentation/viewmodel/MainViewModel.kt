package com.example.curcon.feature.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.curcon.core.constant.AppConstant
import com.example.curcon.core.data.currency_rate.entity.CurrencyRate
import com.example.curcon.core.data.currency_rate.entity.CurrencyRateTrend
import com.example.curcon.core.data.currency_rate.entity.Trend
import com.example.curcon.feature.domain.usecases.ConvertCurrencyUseCase
import com.example.curcon.feature.domain.usecases.GetCurrencyRatesUseCase
import com.example.curcon.widget.UpdateWidgetWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val convertCurrencyUseCase: ConvertCurrencyUseCase,
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase,
    private val application: Application
) : AndroidViewModel(application) {

    private val _convertedAmount = MutableStateFlow<Double>(0.0)
    val convertedAmount: StateFlow<Double> = _convertedAmount.asStateFlow()

    private val _currencies = MutableStateFlow<List<String>>(emptyList())
    val currencies: StateFlow<List<String>> = _currencies.asStateFlow()

    private val _indicativeRate = MutableStateFlow<Double>(0.0)
    val indicativeRate: StateFlow<Double> = _indicativeRate.asStateFlow()

    private val _trends = MutableStateFlow<List<CurrencyRateTrend>>(listOf())
    val trends: StateFlow<List<CurrencyRateTrend>> = _trends.asStateFlow()

    private val _someCountryRate = MutableStateFlow(mapOf<String, Double>())
    val someCountryRate: StateFlow<Map<String, Double>> = _someCountryRate.asStateFlow()

    fun convertCurrency(amount: Double, fromCurrency: String, toCurrency: String) {
        viewModelScope.launch {
            val indicativeRateResult =
                convertCurrencyUseCase.convert(1.toDouble(), fromCurrency, toCurrency)
            val result = indicativeRateResult * amount
            _indicativeRate.value = indicativeRateResult
            _convertedAmount.value = result
        }
    }

    fun getSupportCurrencies() {
        viewModelScope.launch {
            val result = getCurrencyRatesUseCase.getSupportCurrencies()
            _currencies.value = result
        }
    }


    fun getDataFromLocal() {
        viewModelScope.launch {
            val res = getCurrencyRatesUseCase.getAllExchangeRateFromLocal()
            val result = mutableListOf<String>()
            for (item in res) {
                result.add(item.key)
            }
            _currencies.value = result
        }
    }

    fun getSomeCountriesCurrencyRate(base: String) {
        val result = mutableMapOf<String, Double>()
        viewModelScope.launch {
            var rates = getCurrencyRatesUseCase.getAllExchangeRateFromLocal()
            if (rates.isEmpty()) {
                val newData = getCurrencyRatesUseCase.getNewRateExchangeAPIResponse()
                rates = newData.rates
            }
            for (item in AppConstant.famousCountries) {
                result[item] = (rates[item]!! / rates[base]!!)
            }
            saveRates(rates)
            _someCountryRate.value = result
        }
    }

    fun clearModelData() {
        _indicativeRate.value = 0.0
        _convertedAmount.value = 0.0
    }

    private suspend fun saveRates(rates: Map<String, Double>) {
        val ratesList = rates.map { (currencyCode, rate) ->
            CurrencyRate(
                currencyCode = currencyCode,
                rate = rate,
            )
        }

        withContext(Dispatchers.IO) {
            getCurrencyRatesUseCase.saveNewRatesToLocal(ratesList)
            // Cập nhật widget
            val workRequest = OneTimeWorkRequestBuilder<UpdateWidgetWorker>().build()
            WorkManager.getInstance(application).enqueue(workRequest)
        }
    }

    fun compareAndSaveRates() {
        val result = mutableListOf<CurrencyRateTrend>()
        viewModelScope.launch(Dispatchers.IO) {
            val newResponse = getCurrencyRatesUseCase.getNewRateExchangeAPIResponse()

            for ((currencyCode, newRate) in newResponse.rates) {
                val oldRate = getCurrencyRatesUseCase.getCurrencyRateFromLocal(currencyCode)

                val trend = when {
                    newRate > oldRate.rate -> Trend.UP
                    newRate < oldRate.rate -> Trend.DOWN
                    else -> Trend.UNCHANGED
                }

                result.add(
                    CurrencyRateTrend(
                        currencyCode = currencyCode,
                        currentRate = newRate,
                        previousRate = oldRate.rate ?: newRate,
                        trend = trend
                    )
                )
            }
            saveRates(newResponse.rates)
            _trends.value = result
        }
    }
}
