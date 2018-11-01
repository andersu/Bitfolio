package com.zredna.bitfolio.service

import com.zredna.binanceapiclient.BinanceApiClient
import com.zredna.bitfolio.domain.converter.BinanceBalanceDtoConverter
import com.zredna.bitfolio.domain.converter.BinanceMarketSummaryDtoConverter
import com.zredna.bitfolio.domain.model.Balance
import com.zredna.bitfolio.domain.model.MarketSummary

class BinanceService(
        private val binanceApiClient: BinanceApiClient,
        private val binanceBalanceDtoConverter: BinanceBalanceDtoConverter,
        private val binanceMarketSummaryDtoConverter: BinanceMarketSummaryDtoConverter
) {
    suspend fun getBalances(): List<Balance> {
        val accountInformation = binanceApiClient.getAccountInformation().await()
        return binanceBalanceDtoConverter
                .convertToModels(accountInformation.balances)
                .filter { balance -> balance.balance > 0 }
    }

    suspend fun getMarketSummaries(): List<MarketSummary> {
        val marketSummaries = binanceApiClient.getMarketSummaries().await()
        return binanceMarketSummaryDtoConverter.convertToModels(marketSummaries)
    }
}
