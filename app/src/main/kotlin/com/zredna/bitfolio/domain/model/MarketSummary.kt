package com.zredna.bitfolio.domain.model

data class MarketSummary(
        val tradingPair: Pair<String, String>,
        val last: Double
)