package com.zredna.bitfolio.account

data class MarketSummary(
        val tradingPair: Pair<String, String>,
        val last: Double
)