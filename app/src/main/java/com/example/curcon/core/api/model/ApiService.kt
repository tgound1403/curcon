package com.example.curcon.core.api.model

import com.example.curcon.core.api.ExchangeRateAPI
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

//import retrofit2.converter

class ApiService() {
    companion object {
        //TODO: Move key to secured place
        private const val BASE_URL = "https://api.exchangeratesapi.io/v1/"

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(client)
                .build()
        }

        val exchangeRateAPI: ExchangeRateAPI by lazy {
            retrofit.create(ExchangeRateAPI::class.java)
        }
    }
}