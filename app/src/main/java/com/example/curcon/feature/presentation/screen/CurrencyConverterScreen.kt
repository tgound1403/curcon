package com.example.curcon.feature.presentation.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.curcon.feature.presentation.component.AnimatedText
import com.example.curcon.feature.presentation.component.HomeConverter
import com.example.curcon.feature.presentation.component.RateInformation
import com.example.curcon.feature.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CurrencyConverterScreen(viewModel: MainViewModel) {
    var amount by remember { mutableDoubleStateOf(0.0) }
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("VND") }
    val convertedAmount by viewModel.convertedAmount.collectAsState()
    val supportCurrency by viewModel.currencies.collectAsState()
    val indicativeRate by viewModel.indicativeRate.collectAsState()
    val someCountryRates by viewModel.someCountryRate.collectAsState()
    val currencyTrends by viewModel.trends.collectAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    SideEffect { viewModel.getDataFromLocal() }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedText()
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Column {
                    HomeConverter(
                        currencies = supportCurrency,
                        fromCurrency = fromCurrency,
                        toCurrency = toCurrency,
                        onFromCurrencyChanged = {
                            fromCurrency = it; viewModel.getSomeCountriesCurrencyRate(it)
                        },
                        onToCurrencyChanged = {
                            toCurrency = it; viewModel.convertCurrency(
                            amount,
                            fromCurrency,
                            toCurrency
                        )
                        },
                        amount = amount,
                        onAmountChanged = {
                            amount = it.toDouble(); viewModel.getSomeCountriesCurrencyRate(
                            fromCurrency
                        )
                        },
                        onChanged = {
                            viewModel.convertCurrency(amount, fromCurrency, toCurrency)
                        },
                        convertedAmount = convertedAmount,
                        onSwap = {
                            val tempCurr = fromCurrency
                            fromCurrency = toCurrency
                            toCurrency = tempCurr
                            amount = 0.0
                            viewModel.clearModelData()
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (indicativeRate != 0.0 && amount != 0.0) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.LightGray)
                                    .clickable {
                                        if (!viewModel.isNetworkAvailable()) {
                                            Log.d("NETWORK CHECK", "NO NETWORK")
                                            scope.launch(Dispatchers.Main) {
                                                snackbarHostState.showSnackbar(
                                                    message = "We need internet connection to update",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        } else {
                                            viewModel.compareAndSaveRates()
                                            Log.d("NETWORK CHECK", "NO NETWORK")
                                        }
                                    }) {
                                Text("Get latest rates", modifier = Modifier.padding(8.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            RateInformation(
                                fromCurrency,
                                indicativeRate,
                                toCurrency,
                                someCountryRates,
                                currencyTrends
                            )
                        }
                    } else {
                        Text("Input amount to see more information")
                    }
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
