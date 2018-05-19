package com.zredna.bittrex.apiclient

import com.zredna.bittrex.apiclient.dto.GetBalancesResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface BittrexAccountApi {
    @GET("getbalances")
    fun getBalances(
            @Query("apikey") apiKey: String,
            @Query("nonce") nonce: String
    ): Call<GetBalancesResponseDto>
}