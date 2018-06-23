package com.zredna.bitfolio

import com.zredna.bitfolio.db.datamodel.BalanceInBtc
import com.zredna.bitfolio.model.Balance

class BtcBalanceCalculator {

    fun calculateBalancesInBtc(
            balancesInNativeCurrency: List<Balance>,
            marketSummaries: List<MarketSummary>
    ): List<BalanceInBtc> {
        return balancesInNativeCurrency.mapNotNull { balance ->
            marketSummaries.find {
                it.tradingPair.second == balance.currency
            }?.last?.let {
                BalanceInBtc(
                        balance.currency,
                        (it * balance.balance).roundTo8()
                )
            }
        }
    }
}