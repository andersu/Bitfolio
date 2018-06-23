package com.zredna.bitfolio.repository

import android.arch.lifecycle.LiveData
import com.zredna.binanceapiclient.BinanceApiClient
import com.zredna.bitfolio.model.Balance
import com.zredna.bitfolio.db.datamodel.BalanceInBtc
import com.zredna.bitfolio.BtcBalanceCalculator
import com.zredna.bitfolio.ExchangeName
import com.zredna.bitfolio.MarketSummary
import com.zredna.bitfolio.converter.BinanceBalanceDtoConverter
import com.zredna.bitfolio.converter.BittrexBalanceDtoConverter
import com.zredna.bitfolio.converter.BittrexMarketSummaryDtoConverter
import com.zredna.bitfolio.db.BalanceDao
import com.zredna.bitfolio.roundTo8
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
        private val btcBalanceCalculator: BtcBalanceCalculator,
        private val exchangeRepository: ExchangeRepository
) {
    private val balancesInBtc = balanceDao.getBalances()

    fun loadBalances(): LiveData<List<BalanceInBtc>> {
        fetchFromNetwork()
        return balancesInBtc
    }

    private fun fetchFromNetwork() {
        val getBalancesFromBittrex =
                if (!exchangeRepository.containsCredentialsForExchange(ExchangeName.BITTREX.name)) {
                    Single.just(emptyList())
                } else {
                    getBalancesFromBittrex()
                }

        Single.zip(
                getBalancesFromBittrex,
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
        // For BTC we do not have to calculate the balance
        val bitcoinBalance = balances
                .filter { it.currency == "BTC" }
                .map { BalanceInBtc(it.currency, it.balance.roundTo8()) }
                .first()

        // For all other balances, multiply them with their current market price in BTC
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
        // Add all balances from Bittrex
        val balances = mutableListOf<Balance>()
        balances.addAll(bittrexBalances)

        // Add Binance balances to existing Bittrex balances
        for (balance in binanceBalances) {
            balances.find { it.currency == balance.currency }?.let {
                it.balance += balance.balance
            } ?: balances.add(balance)
        }

        return balances
    }
}