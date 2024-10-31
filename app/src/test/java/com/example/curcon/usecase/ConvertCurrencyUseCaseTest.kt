package com.example.curcon.usecase

import com.example.curcon.feature.domain.repositories.ExchangeRateRepository
import com.example.curcon.feature.domain.usecases.ConvertCurrencyUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class ConvertCurrencyUseCaseTest {
    @Mock
    private lateinit var repository: ExchangeRateRepository
    private lateinit var convertCurrencyUseCase: ConvertCurrencyUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        convertCurrencyUseCase = ConvertCurrencyUseCase(repository)
    }

    @Test
    fun `convert should return correct converted amount`() = runBlocking {
        // Given
        val mockRates = mapOf(
            "USD" to 1.0,
            "EUR" to 0.85,
            "GBP" to 0.73
        )
        `when`(repository.getAllExchangeRateDataFromLocal()).thenReturn(mockRates)

        // When
        val result = convertCurrencyUseCase.convert(100.0, "USD", "EUR")

        // Then
        assertEquals(85.0, result, 0.01)
    }

    @Test
    fun `convert should handle unknown currencies`() = runBlocking {
        // Given
        val mockRates = mapOf(
            "USD" to 1.0,
            "EUR" to 0.85
        )
        `when`(repository.getAllExchangeRateDataFromLocal()).thenReturn(mockRates)

        // When
        val result = convertCurrencyUseCase.convert(100.0, "USD", "XXX")

        // Then
        assertEquals(100.0, result, 0.01)
    }
}
