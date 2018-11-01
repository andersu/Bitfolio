package com.zredna.binanceapiclient

import com.zredna.binanceapiclient.dto.AccountInformationDto
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

internal interface BinanceAccountApi {

    @GET("api/v3/account")
    fun getAccountInformation(): Deferred<AccountInformationDto>
}