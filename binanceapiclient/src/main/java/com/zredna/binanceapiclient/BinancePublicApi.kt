package com.zredna.binanceapiclient

import com.zredna.binanceapiclient.dto.MarketSummaryDto
import retrofit2.Call
import retrofit2.http.GET

interface BinancePublicApi {
    @GET("api/v1/ticker/allPrices")
    fun getAllPrices(): Call<List<MarketSummaryDto>>
}