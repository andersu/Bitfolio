package com.zredna.bitfolio.account.service

import com.zredna.bitfolio.account.MarketSummary

interface MarketService {
    fun getMarketSummaries(onSuccess: (List<MarketSummary>) -> Unit, onFailure: () -> Unit)
}