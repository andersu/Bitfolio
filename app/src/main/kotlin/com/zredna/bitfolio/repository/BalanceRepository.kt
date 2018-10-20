package com.zredna.bitfolio.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.zredna.bitfolio.BtcBalanceCalculator
import com.zredna.bitfolio.ExchangeName
import com.zredna.bitfolio.MarketSummary
import com.zredna.bitfolio.db.BalanceDao
import com.zredna.bitfolio.db.datamodel.BalanceInBtc
import com.zredna.bitfolio.model.Balance
import com.zredna.bitfolio.extensions.roundTo8
import com.zredna.bitfolio.service.BinanceService
import com.zredna.bitfolio.service.BittrexService
import io.reactivex.Single
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers

class BalanceRepository(
        private val bittrexService: BittrexService,
        private val binanceService: BinanceService,
        private val balanceDao: BalanceDao,
        private val btcBalanceCalculator: BtcBalanceCalculator,
        private val exchangeRepository: ExchangeRepository
) {
    private val balancesInBtc = balanceDao.getBalances()
    private val balancesInBtcResource = MediatorLiveData<Resource<List<BalanceInBtc>>>()

    init {
        // When the balancesInBtc livedata is updated, balancesInBtcResource will be updated as well
        balancesInBtcResource.addSource(balancesInBtc) {
            balancesInBtcResource.value = Resource.success(it)
        }
    }

    fun loadBalances(): LiveData<Resource<List<BalanceInBtc>>> {
        // Start fetching new balances from the network
        fetchFromNetwork()

        // While fetching return the livedata
        // with the balances we already have in the local database
        return balancesInBtcResource
    }

    private fun fetchFromNetwork() {
        val bittrexBalances =
                if (exchangeRepository.containsCredentialsForExchange(ExchangeName.BITTREX.name)) {
                    bittrexService.getBalances()
                } else {
                    Single.just(emptyList())
                }

        val binanceBalances =
                if (exchangeRepository.containsCredentialsForExchange(ExchangeName.BINANCE.name)) {
                    binanceService.getBalances()
                } else {
                    Single.just(emptyList())
                }

        Single.zip(
                bittrexBalances,
                binanceBalances,
                bittrexService.getMarketSummaries(),
                binanceService.getMarketSummaries(),
                Function4<List<Balance>, List<Balance>, List<MarketSummary>, List<MarketSummary>, List<BalanceInBtc>> { bittrexBalances, binanceBalances, bittrexMarketSummaries, binanceMarketSummaries ->
                    val bittrexBalancesInBtc = calculateBalancesInBtc(bittrexBalances, bittrexMarketSummaries)
                    val binanceBalanceInBtc = calculateBalancesInBtc(binanceBalances, binanceMarketSummaries)
                    mergeBalancesInBtc(bittrexBalancesInBtc, binanceBalanceInBtc)
                })
                .subscribeOn(Schedulers.io())
                .doOnSuccess { balancesInBtc ->
                    // Inserting in the database will trigger an update on the livedata
                    balanceDao.insertBalances(balancesInBtc)
                }
                .onErrorResumeNext {
                    print(it)
                    Single.just(emptyList())
                }
                .subscribe()
    }

    private fun calculateBalancesInBtc(
            balances: List<Balance>,
            marketSummaries: List<MarketSummary>
    ): List<BalanceInBtc> {
        // For BTC we do not have to calculate the balance
        val bitcoinBalance = balances
                .asSequence()
                .filter { it.currency == "BTC" }
                .map { BalanceInBtc(it.currency, it.balance.roundTo8()) }
                .firstOrNull()

        // For all other balances, multiply them with their current market price in BTC
        val allBalancesInBtc = btcBalanceCalculator
                .calculateBalancesInBtc(
                        balances,
                        marketSummaries
                ).toMutableList()

        bitcoinBalance?.let {
            allBalancesInBtc.add(bitcoinBalance)
        }

        return allBalancesInBtc.sortedByDescending { it.balanceInBtc }
    }

    // Takes two lists of balances in btc and merges the contents
    // If both lists contain elements of the same currency the resulting list will
    // show the sum for that currency.
    private fun mergeBalancesInBtc(
            bittrexBalancesInBtc: List<BalanceInBtc>,
            binanceBalancesInBtc: List<BalanceInBtc>
    ): List<BalanceInBtc> {
        // Add all balances from Bittrex
        val balancesInBtc = mutableListOf<BalanceInBtc>()
        balancesInBtc.addAll(bittrexBalancesInBtc)

        // For currencies in both lists, add to existing Bittrex balances.
        // For new currencies, add them to the list.
        for (balance in binanceBalancesInBtc) {
            balancesInBtc.find { it.currency == balance.currency }?.let {
                it.balanceInBtc += balance.balanceInBtc
            } ?: balancesInBtc.add(balance)
        }

        balancesInBtc.forEach { it.balanceInBtc.roundTo8() }

        return balancesInBtc
    }
}