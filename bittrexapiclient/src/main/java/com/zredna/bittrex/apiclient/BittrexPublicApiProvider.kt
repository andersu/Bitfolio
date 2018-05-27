package com.zredna.bittrex.apiclient

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://bittrex.com/api/v1.1/public/"

internal class BittrexPublicApiProvider {
    private val httpClientBuilder = OkHttpClient.Builder()

    fun provideBittrexPublicApi(): BittrexPublicApi {
        val httpClient = initClient()

        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(BittrexPublicApi::class.java)
    }

    fun setLoggingEnabled(): BittrexPublicApiProvider {
        val httpLoggingInterceptor = HttpLoggingInterceptor(ApiLogger())
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(httpLoggingInterceptor)

        return this
    }

    private fun initClient(): OkHttpClient {
        return httpClientBuilder.build()
    }
}