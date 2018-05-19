package com.zredna.bitfolio.account.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.zredna.bitfolio.account.AppExecutors
import com.zredna.bitfolio.account.Balance
import com.zredna.bitfolio.account.BalanceInBtc
import com.zredna.bitfolio.account.BtcBalanceCalculator
import com.zredna.bitfolio.account.api.ApiResponse
import com.zredna.bitfolio.account.api.bittrex.BittrexAccountApi
import com.zredna.bitfolio.account.converter.BittrexBalanceDtoConverter
import com.zredna.bitfolio.account.db.BalanceDao
import com.zredna.bitfolio.account.service.BalanceService
import com.zredna.bitfolio.account.service.MarketService
import com.zredna.bitfolio.account.vo.Resource
import com.zredna.bittrex.apiclient.dto.GetBalancesResponseDto
import java.util.*

private const val BITTREX_API_KEY = "d89007c1259747ecb8ec3f7dca70a976"

class BalanceRepository(
        private val appExecutors: AppExecutors,
        private val bittrexAccountApi: BittrexAccountApi,
        private val bittrexBalanceDtoConverter: BittrexBalanceDtoConverter,
        private val binanceBalanceService: BalanceService,
        private val binanceMarketService: MarketService,
        private val balanceDao: BalanceDao,
        private val btcBalanceCalculator: BtcBalanceCalculator
) {

    private val balances = MediatorLiveData<List<BalanceInBtc>>()

    fun loadBalances(): LiveData<Resource<List<Balance>>> {
        return object : NetworkBoundResource<List<Balance>, GetBalancesResponseDto>(appExecutors) {
            override fun saveCallResult(item: GetBalancesResponseDto) {
                val bittrexBalances = bittrexBalanceDtoConverter
                        .convertToModels(item.result)
                        .filter { it.balance > 0 }
                balanceDao.insertBalances(bittrexBalances)
            }

            override fun shouldFetch(data: List<Balance>?): Boolean {
                // TODO
                return true
            }

            override fun loadFromDb(): LiveData<List<Balance>> {
                return balanceDao.getBalances()
            }

            override fun createCall(): LiveData<ApiResponse<GetBalancesResponseDto>> {
                val nonce = Date().time
                return bittrexAccountApi.getBalances(BITTREX_API_KEY, nonce.toString())
            }
        }.asLiveData()
    }

    /*fun refreshBalances(onSuccess: (List<BalanceInBtc>) -> Unit) {
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