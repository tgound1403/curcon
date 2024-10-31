package com.example.curcon.di

import com.example.curcon.core.api.model.ApiService
import com.example.curcon.feature.data.source.local.ExchangeRateLocalDataSource
import com.example.curcon.feature.data.source.remote.ExchangeRateRemoteDataSource
import com.example.curcon.feature.data.source.remote.ExchangeRateRemoteDataSourceImpl
import com.example.curcon.feature.domain.repositories.ExchangeRateRepository
import com.example.curcon.feature.domain.repositories.ExchangeRateRepositoryImpl
import com.example.curcon.feature.domain.usecases.ConvertCurrencyUseCase
import com.example.curcon.feature.domain.usecases.GetCurrencyRatesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideApiService(): ApiService {
        return ApiService()
    }

    @Provides
    fun provideCurrencyDataSource(): ExchangeRateRemoteDataSource {
        return ExchangeRateRemoteDataSourceImpl()
    }

    @Provides
    fun provideCurrencyRepository(
        localDataSource: ExchangeRateLocalDataSource,
        remoteDataSource: ExchangeRateRemoteDataSource
    ): ExchangeRateRepository {
        return ExchangeRateRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Provides
    fun provideConvertCurrencyUseCase(repository: ExchangeRateRepository): ConvertCurrencyUseCase {
        return ConvertCurrencyUseCase(repository)
    }

    @Provides
    fun provideGetSupportCurrenciesUseCase(repository: ExchangeRateRepository): GetCurrencyRatesUseCase {
        return GetCurrencyRatesUseCase(repository)
    }
}

