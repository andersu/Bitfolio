package com.zredna.bitfolio.service

import com.zredna.bitfolio.domain.converter.BittrexBalanceDtoConverter
import com.zredna.bitfolio.domain.converter.BittrexMarketSummaryDtoConverter
import com.zredna.bitfolio.domain.model.Balance
import com.zredna.bitfolio.domain.model.MarketSummary
import com.zredna.bittrex.apiclient.BittrexApiClient

class BittrexService(
        private val bittrexApiClient: BittrexApiClient,
        private val bittrexBalanceDtoConverter: BittrexBalanceDtoConverter,
        private val bittrexMarketSummaryDtoConverter: BittrexMarketSummaryDtoConverter
) {
    suspend fun getBalances(): List<Balance> {
        val getBalancesResponseDto = bittrexApiClient.getBalances().await()
        if (getBalancesResponseDto.success) {
            getBalancesResponseDto.result?.let { balanceDtos ->
                return bittrexBalanceDtoConverter
                        .convertToModels(balanceDtos)
                        .filter { it.balance > 0 }
            }
        } else {
            throw Exception("Failed to get balances from Bittrex")
        }

        return emptyList()
    }

    suspend fun getMarketSummaries(): List<MarketSummary> {
        val getMarketSummariesResponseDto = bittrexApiClient.getMarketSummaries().await()
        return bittrexMarketSummaryDtoConverter
                .convertToModels(getMarketSummariesResponseDto.result)
    }
}