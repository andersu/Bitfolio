package com.zredna.bitfolio.service

import com.zredna.binanceapiclient.BinanceApiClient
import com.zredna.bitfolio.domain.model.MarketSummary
import com.zredna.bitfolio.domain.converter.BinanceBalanceDtoConverter
import com.zredna.bitfolio.domain.converter.BinanceMarketSummaryDtoConverter
import com.zredna.bitfolio.domain.model.Balance
import io.reactivex.Single

class BinanceService(
        private val binanceApiClient: BinanceApiClient,
        private val binanceBalanceDtoConverter: BinanceBalanceDtoConverter,
        private val binanceMarketSummaryDtoConverter: BinanceMarketSummaryDtoConverter
) {
    fun getBalances(): Single<List<Balance>> {
        return binanceApiClient.getAccountInformation()
                .flatMap {
                    val binanceBalances = binanceBalanceDtoConverter
                            .convertToModels(it.balances)
                            .filter { balance -> balance.balance > 0 }
                    Single.just(binanceBalances)
                }
    }

    fun getMarketSummaries(): Single<List<MarketSummary>> {
        return binanceApiClient.getMarketSummaries()
                .flatMap {
                    val marketSummaries = binanceMarketSummaryDtoConverter
                            .convertToModels(it)
                    Single.just(marketSummaries)
                }
    }
}
