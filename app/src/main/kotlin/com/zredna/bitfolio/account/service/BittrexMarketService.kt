package com.zredna.bitfolio.account.service
/*
import com.zredna.bitfolio.account.MarketSummary
import com.zredna.bitfolio.account.converter.BittrexMarketSummaryDtoConverter
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