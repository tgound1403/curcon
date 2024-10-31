package com.example.curcon.core.data.currency_rate

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.curcon.core.data.currency_rate.entity.CurrencyRate

@Dao
interface CurrencyRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<CurrencyRate>)

    @Query("SELECT * FROM currency_rates")
    suspend fun getAllRates(): List<CurrencyRate>

    @Query("SELECT * FROM currency_rates WHERE currencyCode = :code")
    suspend fun getRateForCurrency(code: String): CurrencyRate?
}
