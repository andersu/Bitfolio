package com.zredna.bittrex.apiclient

import com.zredna.bittrex.apiclient.dto.GetMarketSummariesResponseDto
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET

internal interface BittrexPublicApi {
    @GET("getmarketsummaries")
    fun getMarketSummaries(): Single<GetMarketSummariesResponseDto>
}