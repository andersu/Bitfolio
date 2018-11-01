package com.zredna.binanceapiclient

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.binance.com/"

internal class BinancePublicApiProvider {
    private val httpClientBuilder = OkHttpClient.Builder()

    fun provideBinancePublicApi(): BinancePublicApi {
        val httpClient = initClient()

        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(BinancePublicApi::class.java)
    }

    fun setLoggingEnabled(): BinancePublicApiProvider {
        val httpLoggingInterceptor = HttpLoggingInterceptor(ApiLogger())
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(httpLoggingInterceptor)

        return this
    }

    private fun initClient(): OkHttpClient {
        return httpClientBuilder.build()
    }
}