package com.zredna.bitfolio.account

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