package com.zredna.binanceapiclient

import com.zredna.binanceapiclient.dto.AccountInformationDto
import com.zredna.binanceapiclient.dto.MarketSummaryDto
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BinanceApiClient private constructor(
        private val binanceAccountApi: BinanceAccountApi,
        private val binancePublicApi: BinancePublicApi
) {

    fun getAccountInformation(): Single<AccountInformationDto> {
        return binanceAccountApi.getAccountInformation()
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

        fun build(binanceCredentialsProvider: BinanceCredentialsProvider): BinanceApiClient {
            binanceAccountApiProvider.setCredentialsProvider(binanceCredentialsProvider)
            return BinanceApiClient(
                    binanceAccountApiProvider.provideBinanceApi(),
                    binancePublicApiProvider.provideBinancePublicApi()
            )
        }
    }
}
