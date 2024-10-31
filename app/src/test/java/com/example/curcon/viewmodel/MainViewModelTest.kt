package com.example.curcon.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.curcon.feature.domain.usecases.ConvertCurrencyUseCase
import com.example.curcon.feature.domain.usecases.GetCurrencyRatesUseCase
import com.example.curcon.feature.presentation.viewmodel.MainViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var convertCurrencyUseCase: ConvertCurrencyUseCase

    private lateinit var getCurrencyRatesUseCase: GetCurrencyRatesUseCase

    private lateinit var application: Application

    private lateinit var viewModel: MainViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        convertCurrencyUseCase = mockk()
        getCurrencyRatesUseCase = mockk()
        application = mockk()

        viewModel = MainViewModel(
            convertCurrencyUseCase,
            getCurrencyRatesUseCase,
            application
        )
    }

    @Test
    fun `convertCurrency should update convertedAmount and indicativeRate`() = runTest {
        // Given
        val amount = 100.0
        val fromCurrency = "USD"
        val toCurrency = "EUR"
        val indicativeRate = 0.85
        val expectedResult = amount * indicativeRate

        coEvery { convertCurrencyUseCase.convert(1.0, fromCurrency, toCurrency) } returns indicativeRate

        // When
        viewModel.convertCurrency(amount, fromCurrency, toCurrency)

        // Then
        assertEquals(expectedResult, viewModel.convertedAmount.value)
        assertEquals(indicativeRate, viewModel.indicativeRate.value)
    }

    @Test
    fun `getSupportCurrencies should update currencies list`() = runTest {
        // Given
        val currencies = listOf("USD", "EUR", "GBP")
        coEvery { getCurrencyRatesUseCase.getSupportCurrencies() } returns currencies

        // When
        viewModel.getSupportCurrencies()

        // Then
        assertEquals(currencies, viewModel.currencies.value)
    }

    @Test
    fun `clearModelData should reset values`() {
        // When
        viewModel.clearModelData()

        // Then
        assertEquals(0.0, viewModel.convertedAmount.value)
        assertEquals(0.0, viewModel.indicativeRate.value)
    }
}
