package com.zredna.bitfolio.account.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.zredna.binanceapiclient.BinanceApiClient
import com.zredna.bitfolio.account.Balance
import com.zredna.bitfolio.account.BalanceInBtc
import com.zredna.bitfolio.account.BtcBalanceCalculator
import com.zredna.bitfolio.account.converter.BinanceBalanceDtoConverter
import com.zredna.bitfolio.account.converter.BittrexBalanceDtoConverter
import com.zredna.bitfolio.account.converter.BittrexMarketSummaryDtoConverter
import com.zredna.bitfolio.account.db.BalanceDao
import com.zredna.bitfolio.account.roundTo8
import com.zredna.bittrex.apiclient.BittrexApiClient
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class BalanceRepository(
        private val bittrexApiClient: BittrexApiClient,
        private val bittrexBalanceDtoConverter: BittrexBalanceDtoConverter,
        private val bittrexMarketSummaryDtoConverter: BittrexMarketSummaryDtoConverter,
        private val binanceApiClient: BinanceApiClient,
        private val binanceBalanceDtoConverter: BinanceBalanceDtoConverter,
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
        Single.zip(
                getBalancesFromBittrex(),
                getBalancesFromBinance(),
                BiFunction<List<Balance>, List<Balance>, List<Balance>> { bittrexBalances, binanceBalances ->
                    mergeBalances(bittrexBalances, binanceBalances)
                })
                .subscribeOn(Schedulers.io())
                .doOnSuccess { balances ->
                    balanceDao.insertBalances(balances)
                    calculateBalancesInBtc(balances)
                }
                .subscribe()
    }

    private fun mergeBalances(bittrexBalances: List<Balance>, binanceBalances: List<Balance>): List<Balance> {
        val balances = mutableListOf<Balance>()
        balances.addAll(bittrexBalances)

        for (balance in binanceBalances) {
            balances.find { it.currency == balance.currency }?.let {
                it.balance += balance.balance
            } ?: balances.add(balance)
        }

        return balances
    }

    private fun getBalancesFromBittrex(): Single<List<Balance>> {
        return bittrexApiClient.getBalances()
                .flatMap {
                    val bittrexBalances = bittrexBalanceDtoConverter
                            .convertToModels(it.result)
                            .filter { it.balance > 0 }
                    return@flatMap Single.just(bittrexBalances)
                }
    }

    private fun getBalancesFromBinance(): Single<List<Balance>> {
        return binanceApiClient.getAccountInformation()
                .flatMap {
                    val binanceBalances = binanceBalanceDtoConverter
                            .convertToModels(it.balances)
                            .filter { it.balance > 0 }
                    return@flatMap Single.just(binanceBalances)
                }
    }

    /*binanceBalanceService.getBalances(
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