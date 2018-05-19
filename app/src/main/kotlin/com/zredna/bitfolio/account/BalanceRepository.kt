package com.zredna.bitfolio.account

import android.arch.lifecycle.MutableLiveData
import com.zredna.bitfolio.account.service.BalanceService
import com.zredna.bitfolio.account.service.MarketService

class BalanceRepository(
        private val bittrexBalanceService: BalanceService,
        private val bittrexMarketService: MarketService,
        private val binanceBalanceService: BalanceService,
        private val binanceMarketService: MarketService,
        private val btcBalanceCalculator: BtcBalanceCalculator
) {

    fun loadBalances(): MutableLiveData<List<BalanceInBtc>> {
        val liveData = MutableLiveData<List<BalanceInBtc>>()
        getBalances { liveData.value = it }
        return liveData
    }


    fun refreshBalances(onSuccess: (List<BalanceInBtc>) -> Unit) {
        getBalances(onSuccess)
    }

    private fun getBalances(onSuccess: (List<BalanceInBtc>) -> Unit) {
        getBittrexBalances(
                onSuccess = { bittrexBalances ->
                    getBinanceBalances(
                            onSuccess = { binanceBalances ->
                                val allBalancesInBtc = btcBalanceCalculator.mergeBalances(bittrexBalances, binanceBalances)
                                val sortedBalances = allBalancesInBtc.sortedByDescending {
                                    it.balanceInBtc
                                }

                                onSuccess(sortedBalances)
                            },
                            onFailure = {
                                // TODO
                            }
                    )
                },
                onFailure = {
                    // TODO
                }
        )
    }

    private fun getBittrexBalances(onSuccess: (List<BalanceInBtc>) -> Unit, onFailure: () -> Unit) {
        bittrexBalanceService.getBalances(
                onSuccess = { balances ->
                    bittrexMarketService.getMarketSummaries(
                            onSuccess = { marketSummaries ->
                                val bitcoinBalance = balances
                                        .filter {
                                            it.currency == "BTC"
                                        }.map {
                                            BalanceInBtc(it.currency, it.balance)
                                        }.first()

                                val allBalancesInBtc = btcBalanceCalculator
                                        .calculateBalancesInBtc(
                                                balances,
                                                marketSummaries
                                        ).toMutableList()
                                allBalancesInBtc.add(bitcoinBalance)
                                onSuccess(allBalancesInBtc)
                            },
                            onFailure = onFailure
                    )
                },
                onFailure = {
                    // TODO
                }
        )
    }

    private fun getBinanceBalances(onSuccess: (List<BalanceInBtc>) -> Unit, onFailure: () -> Unit) {
        binanceBalanceService.getBalances(
                onSuccess = { balances ->
                    binanceMarketService.getMarketSummaries(
                            onSuccess = { marketSummaries ->
                                val bitcoinBalance = balances
                                        .filter {
                                            it.currency == "BTC"
                                        }.map {
                                            BalanceInBtc(it.currency, it.balance)
                                        }.first()

                                val allBalancesInBtc = btcBalanceCalculator
                                        .calculateBalancesInBtc(
                                                balances,
                                                marketSummaries
                                        ).toMutableList()
                                allBalancesInBtc.add(bitcoinBalance)
                                onSuccess(allBalancesInBtc)
                            },
                            onFailure = onFailure
                    )
                },
                onFailure = onFailure
        )
    }
}