package com.zredna.bittrex.apiclient

import com.zredna.bittrex.apiclient.dto.GetBalancesResponseDto
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

internal interface BittrexAccountApi {
    @GET("getbalances")
    fun getBalances(@Query("nonce") nonce: String): Deferred<GetBalancesResponseDto>
}