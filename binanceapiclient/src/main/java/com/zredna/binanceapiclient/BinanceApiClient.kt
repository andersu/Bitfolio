package com.zredna.binanceapiclient

import com.zredna.binanceapiclient.dto.AccountInformationDto
import com.zredna.binanceapiclient.dto.MarketSummaryDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BinanceApiClient private constructor(
        private val binanceAccountApi: BinanceAccountApi,
        private val binancePublicApi: BinancePublicApi
) {

    fun getAccountInformation(onSuccess: (AccountInformationDto) -> Unit,
                              onFailure: () -> Unit) {
        binanceAccountApi.getAccountInformation().enqueue(object : Callback<AccountInformationDto> {
            override fun onFailure(call: Call<AccountInformationDto>, t: Throwable?) {
                onFailure()
            }

            override fun onResponse(
                    call: Call<AccountInformationDto>,
                    response: Response<AccountInformationDto>
            ) {
                response.body()?.let {
                    onSuccess(it)
                }
            }
        })
    }

    fun getAllPrices(onSuccess: (List<MarketSummaryDto>) -> Unit,
                     onFailure: () -> Unit) {
        binancePublicApi.getAllPrices().enqueue(object : Callback<List<MarketSummaryDto>> {
            override fun onFailure(call: Call<List<MarketSummaryDto>>, t: Throwable) {
                onFailure()
            }

            override fun onResponse(
                    call: Call<List<MarketSummaryDto>>,
                    response: Response<List<MarketSummaryDto>>
            ) {
                response.body()?.let {
                    onSuccess(it)
                }
            }

        })
    }

    class Builder {
        private val binanceAccountApiProvider = BinanceAccountApiProvider()
        private val binancePublicApiProvider = BinancePublicApiProvider()

        fun setLoggingEnabled(): Builder {
            binanceAccountApiProvider.setLoggingEnabled()

            return this
        }

        fun build(): BinanceApiClient {
            return BinanceApiClient(
                    binanceAccountApiProvider.provideBinanceApi(),
                    binancePublicApiProvider.provideBinancePublicApi()
            )
        }
    }
}
