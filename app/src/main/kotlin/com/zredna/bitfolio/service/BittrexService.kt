package com.zredna.bitfolio.service

import com.zredna.bitfolio.ExchangeName
import com.zredna.bitfolio.MarketSummary
import com.zredna.bitfolio.converter.BittrexBalanceDtoConverter
import com.zredna.bitfolio.converter.BittrexMarketSummaryDtoConverter
import com.zredna.bitfolio.model.Balance
import com.zredna.bittrex.apiclient.BittrexApiClient
import io.reactivex.Single

class BittrexService(
        private val bittrexApiClient: BittrexApiClient,
        private val bittrexBalanceDtoConverter: BittrexBalanceDtoConverter,
        private val bittrexMarketSummaryDtoConverter: BittrexMarketSummaryDtoConverter
) {
    fun getBalances(): Single<List<Balance>> {
        return bittrexApiClient.getBalances()
                .flatMap {
                    if (it.success) {
                        it.result?.let { balanceDtos ->
                            val bittrexBalances = bittrexBalanceDtoConverter
                                    .convertToModels(balanceDtos)
                                    .filter { balance -> balance.balance > 0 }
                            Single.just(bittrexBalances)
                        }
                    } else {
                        // TODO: Proper error
                        error(it.message ?: "")
                    }
                }
    }

    fun getMarketSummaries(): Single<List<MarketSummary>> {
        return bittrexApiClient.getMarketSummaries()
                .flatMap { getMarketSummariesResponseDto ->
                    val marketSummaries = bittrexMarketSummaryDtoConverter
                            .convertToModels(getMarketSummariesResponseDto.result)
                    Single.just(marketSummaries)
                }
    }
}