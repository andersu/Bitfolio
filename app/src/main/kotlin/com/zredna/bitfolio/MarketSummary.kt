package com.zredna.bitfolio

data class MarketSummary(
        val tradingPair: Pair<String, String>,
        val last: Double
)