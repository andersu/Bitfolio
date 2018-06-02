package com.zredna.bitfolio.service

import com.zredna.binanceapiclient.BinanceApiClient
import com.zredna.bitfolio.MarketSummary
import com.zredna.bitfolio.converter.BinanceMarketSummaryDtoConverter

class BinanceMarketService(
        private val binanceApiClient: BinanceApiClient,
        private val marketSummaryDtoConverter: BinanceMarketSummaryDtoConverter
) : MarketService {

    override fun getMarketSummaries(onSuccess: (List<MarketSummary>) -> Unit,
                                    onFailure: () -> Unit) {
        binanceApiClient.getAllPrices(
                onSuccess = {
                    // We are only interested in BTC trading pairs for now
                    val marketSummariesBtcTradingPairs = it.filter {
                        it.symbol.endsWith("BTC")
                    }
                    onSuccess(marketSummaryDtoConverter.convertToModels(marketSummariesBtcTradingPairs))
                },
                onFailure = onFailure
        )
    }
}