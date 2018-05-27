package com.zredna.bitfolio.account.repository

import android.arch.lifecycle.LiveData
import com.zredna.binanceapiclient.BinanceApiClient
import com.zredna.bitfolio.account.Balance
import com.zredna.bitfolio.account.BalanceInBtc
import com.zredna.bitfolio.account.BtcBalanceCalculator
import com.zredna.bitfolio.account.MarketSummary
import com.zredna.bitfolio.account.converter.BinanceBalanceDtoConverter
import com.zredna.bitfolio.account.converter.BittrexBalanceDtoConverter
import com.zredna.bitfolio.account.converter.BittrexMarketSummaryDtoConverter
import com.zredna.bitfolio.account.db.BalanceDao
import com.zredna.bitfolio.account.roundTo8
import com.zredna.bittrex.apiclient.BittrexApiClient
import io.reactivex.Single
import io.reactivex.functions.Function3
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
    private val balancesInBtc = balanceDao.getBalances()

    fun loadBalances(): LiveData<List<BalanceInBtc>> {
        fetchFromNetwork()
        return balancesInBtc
    }

    private fun fetchFromNetwork() {
        Single.zip(
                getBalancesFromBittrex(),
                getBalancesFromBinance(),
                getMarketSummariesFromBittrex(),
                Function3<List<Balance>, List<Balance>, List<MarketSummary>, List<BalanceInBtc>> { bittrexBalances, binanceBalances, marketSummaries ->
                    val balances = mergeBalances(bittrexBalances, binanceBalances)
                    calculateBalancesInBtc(balances, marketSummaries)
                })
                .subscribeOn(Schedulers.io())
                .doOnSuccess { balancesInBtc ->
                    balanceDao.insertBalances(balancesInBtc)
                }
                .subscribe()
    }


    private fun getBalancesFromBittrex(): Single<List<Balance>> {
        return bittrexApiClient.getBalances()
                .flatMap {
                    val bittrexBalances = bittrexBalanceDtoConverter
                            .convertToModels(it.result)
                            .filter { it.balance > 0 }
                    Single.just(bittrexBalances)
                }
    }

    private fun getBalancesFromBinance(): Single<List<Balance>> {
        return binanceApiClient.getAccountInformation()
                .flatMap {
                    val binanceBalances = binanceBalanceDtoConverter
                            .convertToModels(it.balances)
                            .filter { it.balance > 0 }
                    Single.just(binanceBalances)
                }
    }

    private fun getMarketSummariesFromBittrex(): Single<List<MarketSummary>> {
        return bittrexApiClient.getMarketSummaries()
                .flatMap { getMarketSummariesResponseDto ->
                    val marketSummaries = bittrexMarketSummaryDtoConverter
                            .convertToModels(getMarketSummariesResponseDto.result)
                    Single.just(marketSummaries)
                }
    }

    private fun calculateBalancesInBtc(
            balances: List<Balance>,
            marketSummaries: List<MarketSummary>
    ): List<BalanceInBtc> {
        val bitcoinBalance = balances.filter {
            it.currency == "BTC"
        }.map { BalanceInBtc(it.currency, it.balance.roundTo8()) }.first()

        val allBalancesInBtc = btcBalanceCalculator
                .calculateBalancesInBtc(
                        balances,
                        marketSummaries
                ).toMutableList()
        allBalancesInBtc.add(bitcoinBalance)
        return allBalancesInBtc.sortedByDescending { it.balanceInBtc }
    }

    private fun mergeBalances(
            bittrexBalances: List<Balance>,
            binanceBalances: List<Balance>
    ): List<Balance> {
        val balances = mutableListOf<Balance>()
        balances.addAll(bittrexBalances)

        for (balance in binanceBalances) {
            balances.find { it.currency == balance.currency }?.let {
                it.balance += balance.balance
            } ?: balances.add(balance)
        }

        return balances
    }
}