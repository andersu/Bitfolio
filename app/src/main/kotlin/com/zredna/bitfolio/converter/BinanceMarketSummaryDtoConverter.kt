package com.zredna.bitfolio.converter

import com.zredna.binanceapiclient.dto.MarketSummaryDto
import com.zredna.bitfolio.MarketSummary

class BinanceMarketSummaryDtoConverter : DtoConverter<MarketSummaryDto, MarketSummary>() {
    override fun convertToDto(model: MarketSummary): MarketSummaryDto {
        throw NotImplementedError()
    }

    override fun convertToModel(dto: MarketSummaryDto): MarketSummary {
        val tradingPair = Pair("BTC", dto.symbol.split("BTC").first())
        return MarketSummary(tradingPair, dto.price.toDouble())
    }

}