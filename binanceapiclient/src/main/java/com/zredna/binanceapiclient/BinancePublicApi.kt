package com.zredna.binanceapiclient

import com.zredna.binanceapiclient.dto.MarketSummaryDto
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET

internal interface BinancePublicApi {
    @GET("api/v1/ticker/allPrices")
    fun getAllPrices(): Single<List<MarketSummaryDto>>
}