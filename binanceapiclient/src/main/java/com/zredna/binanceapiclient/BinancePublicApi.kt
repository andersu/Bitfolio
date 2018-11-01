package com.zredna.binanceapiclient

import com.zredna.binanceapiclient.dto.MarketSummaryDto
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

internal interface BinancePublicApi {
    @GET("api/v1/ticker/allPrices")
    fun getAllPrices(): Deferred<List<MarketSummaryDto>>
}