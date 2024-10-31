package com.example.curcon.usecase

import com.example.curcon.core.data.currency_rate.entity.CurrencyRate
import com.example.curcon.feature.domain.repositories.ExchangeRateRepository
import com.example.curcon.feature.domain.usecases.GetCurrencyRatesUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetCurrencyRatesUseCaseTest {
    @Mock
    private lateinit var repository: ExchangeRateRepository
    private lateinit var getCurrencyRatesUseCase: GetCurrencyRatesUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getCurrencyRatesUseCase = GetCurrencyRatesUseCase(repository)
    }

    @Test
    fun `getSupportCurrencies should return sorted list of currencies`() = runBlocking {
        // Given
        val mockRates = mapOf(
            "EUR" to 0.85,
            "USD" to 1.0,
            "GBP" to 0.73
        )
        `when`(repository.getExchangeRates()).thenReturn(mockRates)

        // When
        val result = getCurrencyRatesUseCase.getSupportCurrencies()

        // Then
        assertEquals(listOf("EUR", "GBP", "USD"), result)
    }

    @Test
    fun `getAllExchangeRateFromLocal should return rates from repository`() = runBlocking {
        // Given
        val mockRates = mapOf(
            "USD" to 1.0,
            "EUR" to 0.85
        )
        `when`(repository.getAllExchangeRateDataFromLocal()).thenReturn(mockRates)

        // When
        val result = getCurrencyRatesUseCase.getAllExchangeRateFromLocal()

        // Then
        assertEquals(mockRates, result)
    }

    @Test
    fun `saveNewRatesToLocal should call repository`() = runBlocking {
        // Given
        val rates = listOf(
            CurrencyRate("USD", 1.0),
            CurrencyRate("EUR", 0.85)
        )

        // When
        getCurrencyRatesUseCase.saveNewRatesToLocal(rates)

        // Then
        verify(repository).saveNewRatesToLocal(rates)
    }

    @Test
    fun `getCurrencyRateFromLocal should return correct currency rate`() = runBlocking {
        // Given
        val expectedRate = CurrencyRate("USD", 1.0)
        `when`(repository.getLatestRateOfCurrency("USD")).thenReturn(expectedRate)

        // When
        val result = getCurrencyRatesUseCase.getCurrencyRateFromLocal("USD")

        // Then
        assertEquals(expectedRate, result)
    }
}
