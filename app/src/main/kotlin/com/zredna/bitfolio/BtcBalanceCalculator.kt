package com.zredna.bitfolio

import com.zredna.bitfolio.db.datamodel.BalanceInBtc
import com.zredna.bitfolio.extensions.roundTo8
import com.zredna.bitfolio.domain.model.Balance
import com.zredna.bitfolio.domain.model.MarketSummary

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