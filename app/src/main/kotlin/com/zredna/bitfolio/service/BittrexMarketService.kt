package com.zredna.bitfolio.service
/*
import com.zredna.bitfolio.MarketSummary
import com.zredna.bitfolio.BittrexMarketSummaryDtoConverter
import com.zredna.bitfolio.account.api.bittrex.BittrexApiClient

class BittrexMarketService(
        private val bittrexApiClient: BittrexApiClient,
        private val marketSummaryDtoConverter: BittrexMarketSummaryDtoConverter
): MarketService {

    override fun getMarketSummaries(onSuccess: (List<MarketSummary>) -> Unit,
                           onFailure: () -> Unit) {
        bittrexApiClient.getMarketSummaries(
                onSuccess = {
                    onSuccess(marketSummaryDtoConverter.convertToModels(it.result))
                },
                onFailure = onFailure
        )
    }
}*/