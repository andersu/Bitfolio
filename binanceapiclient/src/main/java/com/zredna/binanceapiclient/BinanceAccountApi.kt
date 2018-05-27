package com.zredna.binanceapiclient

import com.zredna.binanceapiclient.dto.AccountInformationDto
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET

internal interface BinanceAccountApi {

    @GET("api/v3/account")
    fun getAccountInformation(): Single<AccountInformationDto>
}