package com.zredna.bitfolio.repository

import androidx.lifecycle.MutableLiveData
import com.zredna.bitfolio.BaseLiveDataTest
import com.zredna.bitfolio.BtcBalanceCalculator
import com.zredna.bitfolio.ExchangeName
import com.zredna.bitfolio.MarketSummary
import com.zredna.bitfolio.db.BalanceDao
import com.zredna.bitfolio.db.datamodel.BalanceInBtc
import com.zredna.bitfolio.model.Balance
import com.zredna.bitfolio.service.BinanceService
import com.zredna.bitfolio.service.BittrexService
import io.reactivex.Single.just
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock

class BalanceRepositoryTest : BaseLiveDataTest() {
    private lateinit var balanceRepository: BalanceRepository

    @Mock
    private lateinit var bittrexService: BittrexService
    @Mock
    private lateinit var binanceService: BinanceService
    @Mock
    private lateinit var balanceDao: BalanceDao
    @Mock
    private lateinit var btcBalanceCalculator: BtcBalanceCalculator
    @Mock
    private lateinit var exchangeRepository: ExchangeRepository

    @Mock
    private lateinit var bittrexBalances: List<Balance>
    @Mock
    private lateinit var bittrexMarketSummaries: List<MarketSummary>

    @Mock
    private lateinit var binanceBalances: List<Balance>
    @Mock
    private lateinit var binanceMarketSummaries: List<MarketSummary>

    @Test
    fun loadBalancesReturnsBalancesFromDao() {
        val balancesLiveData = MutableLiveData<List<BalanceInBtc>>()
        val balances = emptyList<BalanceInBtc>()
        balancesLiveData.value = balances

        given(bittrexService.getBalances()).willReturn(just(bittrexBalances))
        given(bittrexService.getMarketSummaries()).willReturn(just(bittrexMarketSummaries))
        given(balanceDao.getBalances()).willReturn(balancesLiveData)
        given(exchangeRepository.containsCredentialsForExchange(ExchangeName.BITTREX.name))
                .willReturn(true)
        given(exchangeRepository.containsCredentialsForExchange(ExchangeName.BINANCE.name))
                .willReturn(true)

        given(binanceService.getBalances()).willReturn(just(binanceBalances))
        given(binanceService.getMarketSummaries()).willReturn(just(binanceMarketSummaries))

        balanceRepository = BalanceRepository(
                bittrexService,
                binanceService,
                balanceDao,
                btcBalanceCalculator,
                exchangeRepository
        )

        balanceRepository.loadBalances().observeForever {
            assertEquals(Resource.success(balances), it)
        }
    }

    // TODO: Figure out how to test that new balances from the network is inserted using Dao
}