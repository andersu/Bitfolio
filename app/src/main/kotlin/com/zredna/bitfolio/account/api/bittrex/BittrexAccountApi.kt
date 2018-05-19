package com.zredna.bitfolio.account.api.bittrex

import android.arch.lifecycle.LiveData
import com.zredna.bitfolio.account.api.ApiResponse
import com.zredna.bittrex.apiclient.dto.GetBalancesResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BittrexAccountApi {
    @GET("getbalances")
    fun getBalances(
            @Query("apikey") apiKey: String,
            @Query("nonce") nonce: String
    ): LiveData<ApiResponse<GetBalancesResponseDto>>
}