package com.zredna.bittrex.apiclient

import com.zredna.bittrex.apiclient.dto.GetMarketSummariesResponseDto
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

internal interface BittrexPublicApi {
    @GET("getmarketsummaries")
    fun getMarketSummaries(): Deferred<GetMarketSummariesResponseDto>
}