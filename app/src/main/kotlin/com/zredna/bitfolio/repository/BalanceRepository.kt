package com.zredna.bitfolio.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.zredna.bitfolio.BtcBalanceCalculator
import com.zredna.bitfolio.db.BalanceDao
import com.zredna.bitfolio.db.datamodel.BalanceInBtc
import com.zredna.bitfolio.domain.model.Balance
import com.zredna.bitfolio.domain.model.ExchangeName
import com.zredna.bitfolio.domain.model.MarketSummary
import com.zredna.bitfolio.extensions.roundTo8
import com.zredna.bitfolio.service.BinanceService
import com.zredna.bitfolio.service.BittrexService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.coroutines.CoroutineContext

class BalanceRepository(
        private val bittrexService: BittrexService,
        private val binanceService: BinanceService,
        private val balanceDao: BalanceDao,
        private val btcBalanceCalculator: BtcBalanceCalculator,
        private val exchangeRepository: ExchangeRepository
) : CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

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
        launch(Dispatchers.IO) {

            supervisorScope {
                val bittrexBalances = async {
                    if (exchangeRepository.containsCredentialsForExchange(ExchangeName.BITTREX)) {
                        bittrexService.getBalances()
                    } else {
                        emptyList()
                    }
                }

                val binanceBalances = async {
                    if (exchangeRepository.containsCredentialsForExchange(ExchangeName.BINANCE)) {
                        binanceService.getBalances()
                    } else {
                        emptyList()
                    }
                }

                val bittrexMarketSummaries = async {
                    bittrexService.getMarketSummaries()
                }

                val binanceMarketSummaries = async {
                    binanceService.getMarketSummaries()
                }

                try {
                    val bittrexBalancesInBtc = calculateBalancesInBtc(bittrexBalances.await(),
                            bittrexMarketSummaries.await())
                    val binanceBalanceInBtc = calculateBalancesInBtc(binanceBalances.await(),
                            binanceMarketSummaries.await())
                    val balancesInBtc = mergeBalancesInBtc(bittrexBalancesInBtc, binanceBalanceInBtc)
                    balanceDao.insertBalances(balancesInBtc)
                } catch (exception: Exception) {
                    balancesInBtcResource.postValue(Resource.error("Failed to get balances", null))
                }
            }
        }
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