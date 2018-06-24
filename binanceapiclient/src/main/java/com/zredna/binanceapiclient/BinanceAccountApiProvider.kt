package com.zredna.binanceapiclient

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

private const val BASE_URL = "https://api.binance.com/"
private const val API_KEY = ""
private const val SECRET = ""

class BinanceAccountApiProvider {
    private val httpClientBuilder = OkHttpClient.Builder()

    internal fun provideBinanceApi(): BinanceAccountApi {
        val httpClient = initClient()

        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(BinanceAccountApi::class.java)
    }

    fun setLoggingEnabled(): BinanceAccountApiProvider {
        val httpLoggingInterceptor = HttpLoggingInterceptor(ApiLogger())
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(httpLoggingInterceptor)

        return this
    }

    private fun initClient(): OkHttpClient {
        httpClientBuilder.addInterceptor { chain ->
            val originalRequest = chain.request()
            val originalHttpUrl = originalRequest.url()

            val intermediateUrl = originalHttpUrl.newBuilder()
                    .addQueryParameter("timestamp", "${Date().time}")
                    .addQueryParameter("recvWindow", "60000")
                    .build();

            var totalParams = intermediateUrl.query().toString()
            originalRequest.body()?.let {
                totalParams += it.toString()
            }
            val signature = HashUtil.hmacSha256(totalParams, SECRET)

            val url = intermediateUrl.newBuilder()
                    .addQueryParameter("signature", signature)
                    .build()

            val requestBuilder = originalRequest.newBuilder().url(url)

            requestBuilder.addHeader("X-MBX-APIKEY", API_KEY)

            val request = requestBuilder.build()
            chain.proceed(request)
        }

        return httpClientBuilder.build()
    }
}