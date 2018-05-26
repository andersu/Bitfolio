package com.zredna.bitfolio.account

class BtcBalanceCalculator {

    fun calculateBalancesInBtc(balancesInNativeCurrency: List<Balance>,
                               marketSummaries: List<MarketSummary>
    ): List<BalanceInBtc> {
        return balancesInNativeCurrency
                .map { balance ->
                    return@map marketSummaries.find {
                        it.tradingPair.second == balance.currency
                    }?.last?.let {
                        BalanceInBtc(
                                balance.currency,
                                (it * balance.balance).roundTo8()
                        )
                    }
                }
                .filterNotNull()
    }

    fun mergeBalances(
            firstBalances: List<BalanceInBtc>,
            otherBalances: List<BalanceInBtc>
    ): List<BalanceInBtc> {
        val balances = mutableListOf<BalanceInBtc>()
        balances.addAll(firstBalances)

        otherBalances.forEach { otherBalance ->
            val balanceForCurrency = balances.find {
                it.currency == otherBalance.currency
            }
            if (balanceForCurrency != null) {
                balanceForCurrency.balanceInBtc += otherBalance.balanceInBtc
            } else {
                balances.add(otherBalance)
            }
        }

        return balances
    }
}