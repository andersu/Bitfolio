package com.zredna.bitfolio.service

import com.zredna.bitfolio.MarketSummary

interface MarketService {
    fun getMarketSummaries(onSuccess: (List<MarketSummary>) -> Unit, onFailure: () -> Unit)
}