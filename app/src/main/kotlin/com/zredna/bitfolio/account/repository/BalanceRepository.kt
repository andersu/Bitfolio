package com.zredna.bitfolio.account.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.zredna.bitfolio.account.AppExecutors
import com.zredna.bitfolio.account.Balance
import com.zredna.bitfolio.account.BalanceInBtc
import com.zredna.bitfolio.account.BtcBalanceCalculator
import com.zredna.bitfolio.account.converter.BittrexBalanceDtoConverter
import com.zredna.bitfolio.account.converter.BittrexMarketSummaryDtoConverter
import com.zredna.bitfolio.account.db.BalanceDao
import com.zredna.bitfolio.account.roundTo8
import com.zredna.bittrex.apiclient.BittrexApiClient

class BalanceRepository(
        private val appExecutors: AppExecutors,
        private val bittrexApiClient: BittrexApiClient,
        private val bittrexBalanceDtoConverter: BittrexBalanceDtoConverter,
        private val bittrexMarketSummaryDtoConverter: BittrexMarketSummaryDtoConverter,
        private val balanceDao: BalanceDao,
        private val btcBalanceCalculator: BtcBalanceCalculator
) {
    private val balancesInBtc = MutableLiveData<List<BalanceInBtc>>()

    fun loadBalances(): LiveData<List<BalanceInBtc>> {
        loadFromDb()
        fetchFromNetwork()
        return balancesInBtc
    }

    private fun loadFromDb() {
        val balances = balanceDao.getBalances()
        balances.value?.let {
            calculateBalancesInBtc(it)
        }
    }

    private fun calculateBalancesInBtc(balances: List<Balance>) {
        bittrexApiClient.getMarketSummaries(
                onSuccess = { getMarketSummariesResponseDto ->
                    val bitcoinBalance = balances.filter {
                        it.currency == "BTC"
                    }.map { BalanceInBtc(it.currency, it.balance.roundTo8()) }.first()

                    val marketSummaries = bittrexMarketSummaryDtoConverter
                            .convertToModels(getMarketSummariesResponseDto.result)
                    val allBalancesInBtc = btcBalanceCalculator
                            .calculateBalancesInBtc(
                                    balances,
                                    marketSummaries
                            ).toMutableList()
                    allBalancesInBtc.add(bitcoinBalance)
                    val sortedBalances = allBalancesInBtc.sortedByDescending { it.balanceInBtc }
                    balancesInBtc.postValue(sortedBalances)
                },
                onFailure = {
                    // TODO
                }
        )
    }

    private fun fetchFromNetwork() {
        bittrexApiClient.getBalances(
                onSuccess = {
                    val newBalances = bittrexBalanceDtoConverter
                            .convertToModels(it.result)
                            .filter { it.balance > 0 }
                    appExecutors.diskIO().execute {
                        balanceDao.insertBalances(newBalances)
                        calculateBalancesInBtc(newBalances)
                    }
                },
                onFailure = {
                    // TODO
                }
        )
    }

    /*private fun getBittrexBalances(onSuccess: (List<BalanceInBtc>) -> Unit, onFailure: () -> Unit) {
        bittrexBalanceService.getBalances(
                onSuccess = { balances ->
                    bittrexMarketService.getMarketSummaries(
                            onSuccess = { marketSummaries ->
                                val bitcoinBalance = balances
                                        .filter {
                                            it.currency == "BTC"
                                        }.map {
                                            BalanceInBtc(it.currency, it.balance.roundTo8())
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
    }*/

    /*
    private fun getBinanceBalances(onSuccess: (List<BalanceInBtc>) -> Unit, onFailure: () -> Unit) {
        binanceBalanceService.getBalances(
                onSuccess = { balances ->
                    binanceMarketService.getMarketSummaries(
                            onSuccess = { marketSummaries ->
                                val bitcoinBalance = balances
                                        .filter {
                                            it.currency == "BTC"
                                        }.map {
                                            BalanceInBtc(it.currency, it.balance.roundTo8())
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
    }*/
}