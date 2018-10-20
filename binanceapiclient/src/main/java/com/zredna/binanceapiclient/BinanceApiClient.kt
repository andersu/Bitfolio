package com.zredna.binanceapiclient

import com.zredna.binanceapiclient.dto.AccountInformationDto
import com.zredna.binanceapiclient.dto.MarketSummaryDto
import io.reactivex.Single

class BinanceApiClient private constructor(
        private val binanceAccountApi: BinanceAccountApi,
        private val binancePublicApi: BinancePublicApi
) {

    fun getAccountInformation(): Single<AccountInformationDto> {
        return binanceAccountApi.getAccountInformation()
    }

    fun getMarketSummaries(): Single<List<MarketSummaryDto>> {
        return binancePublicApi.getAllPrices()
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
