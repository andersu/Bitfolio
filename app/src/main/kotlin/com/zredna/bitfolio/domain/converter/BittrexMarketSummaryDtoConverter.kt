package com.zredna.bitfolio.domain.converter

import com.zredna.bitfolio.domain.model.MarketSummary
import com.zredna.bittrex.apiclient.dto.MarketSummaryDto

class BittrexMarketSummaryDtoConverter : DtoConverter<MarketSummaryDto, MarketSummary>() {
    override fun convertToDto(model: MarketSummary): MarketSummaryDto {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun convertToModel(dto: MarketSummaryDto): MarketSummary {
        val tradingPair = Pair("BTC", dto.marketName.split("BTC-").last())
        return MarketSummary(tradingPair, dto.last)
    }
}
