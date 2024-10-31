package com.example.curcon.core.data.currency_rate

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.curcon.core.data.currency_rate.entity.CurrencyRate
import com.example.curcon.core.data.currency_rate.entity.RatesTypeConverter

@Database(entities = [CurrencyRate::class], version = 1)
@TypeConverters(RatesTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyRateDao(): CurrencyRateDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "currency_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}