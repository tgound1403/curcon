package com.example.curcon.repository

import com.example.curcon.core.api.model.ExchangeRateResult
import com.example.curcon.core.data.currency_rate.entity.CurrencyRate
import com.example.curcon.feature.data.source.local.ExchangeRateLocalDataSource
import com.example.curcon.feature.data.source.remote.ExchangeRateRemoteDataSource
import com.example.curcon.feature.domain.repositories.ExchangeRateRepositoryImpl
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class ExchangeRateRepositoryImplTest {

    @Mock
    private lateinit var remoteDataSource: ExchangeRateRemoteDataSource

    @Mock
    private lateinit var localDataSource: ExchangeRateLocalDataSource

    private lateinit var repository: ExchangeRateRepositoryImpl

    private lateinit var mockResult: ExchangeRateResult

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = ExchangeRateRepositoryImpl(remoteDataSource, localDataSource)
        mockResult = mockk<ExchangeRateResult>()
    }

    @Test
    fun getExchangeRatesReturnsRemoteRatesWhenAvailable(): Unit = runBlocking {
        // Given
        val mockRates = mapOf("USD" to 1.0, "EUR" to 0.85)
        mockResult.copy(rates = mockRates)
        `when`(remoteDataSource.getExchangeRates()).thenReturn(mockResult)

        // When
        val result = repository.getExchangeRates()

        // Then
        assertEquals(mockRates, result)
        verify(remoteDataSource).getExchangeRates()
    }

    @Test
    fun `getExchangeRates returns empty map when remote data is null`(): Unit = runBlocking {
        // Given
        `when`(remoteDataSource.getExchangeRates()).thenReturn(null)

        // When
        val result = repository.getExchangeRates()

        // Then
        assertEquals(emptyMap<String, Double>(), result)
        verify(remoteDataSource).getExchangeRates()
    }

    @Test
    fun `getExchangeRateAPIResponse returns remote response`(): Unit = runBlocking {
        // Given
        mockResult.copy(rates = mapOf("USD" to 1.0))
        `when`(remoteDataSource.getExchangeRates()).thenReturn(mockResult)

        // When
        val result = repository.getExchangeRateAPIResponse()

        // Then
        assertEquals(mockResult, result)
        verify(remoteDataSource).getExchangeRates()
    }

    @Test
    fun `getAllCurrencyRateDataFromLocal returns local data`(): Unit = runBlocking {
        // Given
        val mockLocalData = listOf(
            CurrencyRate("USD", 1.0), CurrencyRate("EUR", 0.85)
        )
        `when`(localDataSource.getCurrencyRate()).thenReturn(mockLocalData)

        // When
        val result = repository.getAllCurrencyRateDataFromLocal()

        // Then
        assertEquals(mockLocalData, result)
        verify(localDataSource).getCurrencyRate()
    }

    @Test
    fun `getAllExchangeRateDataFromLocal converts local data to map`(): Unit = runBlocking {
        // Given
        val mockLocalData = listOf(
            CurrencyRate("USD", 1.0), CurrencyRate("EUR", 0.85)
        )
        val expectedMap = mapOf("USD" to 1.0, "EUR" to 0.85)
        `when`(localDataSource.getCurrencyRate()).thenReturn(mockLocalData)

        // When
        val result = repository.getAllExchangeRateDataFromLocal()

        // Then
        assertEquals(expectedMap, result)
        verify(localDataSource).getCurrencyRate()
    }

    @Test
    fun `getLatestRateOfCurrency returns currency rate for specific code`(): Unit = runBlocking {
        // Given
        val currencyCode = "USD"
        val mockRate = CurrencyRate(currencyCode, 1.0)
        `when`(localDataSource.getLatestRateForCurrency(currencyCode)).thenReturn(mockRate)

        // When
        val result = repository.getLatestRateOfCurrency(currencyCode)

        // Then
        assertEquals(mockRate, result)
        verify(localDataSource).getLatestRateForCurrency(currencyCode)
    }

    @Test
    fun `saveNewRatesToLocal saves rates to local data source`() = runBlocking {
        // Given
        val rates = listOf(
            CurrencyRate("USD", 1.0), CurrencyRate("EUR", 0.85)
        )

        // When
        repository.saveNewRatesToLocal(rates)

        // Then
        verify(localDataSource).saveNewRates(rates)
    }
}
