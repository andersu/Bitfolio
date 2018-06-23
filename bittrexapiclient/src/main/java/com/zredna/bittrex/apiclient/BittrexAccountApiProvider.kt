package com.zredna.bittrex.apiclient

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://bittrex.com/api/v1.1/account/"

internal class BittrexAccountApiProvider {
    private val httpClientBuilder = OkHttpClient.Builder()

    private lateinit var credentialsProvider: CredentialsProvider

    fun provideBittrexAccountApi(): BittrexAccountApi {
        val httpClient = initClient()

        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(BittrexAccountApi::class.java)
    }

    fun setLoggingEnabled(): BittrexAccountApiProvider {
        val httpLoggingInterceptor = HttpLoggingInterceptor(ApiLogger())
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(httpLoggingInterceptor)

        return this
    }

    fun setCredentialsProvider(credentialsProvider: CredentialsProvider) {
        this.credentialsProvider = credentialsProvider
    }

    private fun initClient(): OkHttpClient {
        httpClientBuilder.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()

            val credentials = credentialsProvider.getCredentials()
            val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("apikey", credentials.apiKey)
                    .build()
                    .toString()
            requestBuilder.url(url)

            requestBuilder.addHeader("apisign", HashUtil.hmacSha512(url, credentials.secret))

            val request = requestBuilder.build()

            chain.proceed(request)
        }

        return httpClientBuilder.build()
    }
}