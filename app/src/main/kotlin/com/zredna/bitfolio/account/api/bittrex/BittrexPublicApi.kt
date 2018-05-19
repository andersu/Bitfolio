package com.zredna.bittrex.apiclient

import com.zredna.bittrex.apiclient.dto.GetMarketSummariesResponseDto
import retrofit2.Call
import retrofit2.http.GET

internal interface BittrexPublicApi {
    @GET("getmarketsummaries")
    fun getMarketSummaries(): Call<GetMarketSummariesResponseDto>
}