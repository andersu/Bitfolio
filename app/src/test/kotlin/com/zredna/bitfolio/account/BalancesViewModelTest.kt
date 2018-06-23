package com.zredna.bitfolio.account

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.zredna.bitfolio.db.datamodel.BalanceInBtc
import com.zredna.bitfolio.BaseTest
import com.zredna.bitfolio.repository.BalanceRepository
import com.zredna.bitfolio.view.account.balances.BalancesViewModel
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class BalancesViewModelTest: BaseTest() {

    private lateinit var balancesViewModel: BalancesViewModel

    @Mock
    private lateinit var balanceRepository: BalanceRepository

    /*
     * This is required to test LiveData.
     * When setting values Android checks to see what thread the call is made from,
     * and this rule returns the required result to avoid an NPE
     */
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        balancesViewModel = BalancesViewModel(balanceRepository)
    }

    @Test
    fun getBalancesEmptyList() {
        val balancesLiveData = MutableLiveData<List<BalanceInBtc>>()
        val balances = emptyList<BalanceInBtc>()
        given(balanceRepository.loadBalances()).willReturn(balancesLiveData)

        balancesViewModel = BalancesViewModel(balanceRepository)
        balancesLiveData.value = balances

        balancesViewModel.balances.observeForever { assertEquals(it, balances) }
        balancesViewModel.totalBalance.observeForever { assertEquals(0.0, it) }
    }

    @Test
    fun getBalancesNonEmptyList() {
        val balancesLiveData = MutableLiveData<List<BalanceInBtc>>()
        val balances = listOf(
                BalanceInBtc("currency1", 0.10),
                BalanceInBtc("currency2", 0.20)
        )
        given(balanceRepository.loadBalances()).willReturn(balancesLiveData)

        balancesViewModel = BalancesViewModel(balanceRepository)
        balancesLiveData.value = balances

        balancesViewModel.balances.observeForever { assertEquals(it, balances) }
        balancesViewModel.totalBalance.observeForever { assertEquals(0.30, it) }
    }

    @Test
    fun balancesUpdatedStopsRefreshing() {
        balancesViewModel.balancesUpdated()

        balancesViewModel.isRefreshing().observeForever { assertEquals(it, false) }
    }

    @Test
    fun refreshStartsRefreshing() {
        balancesViewModel.refresh()

        balancesViewModel.isRefreshing().observeForever { assertEquals(it, true) }
    }

    @Test
    fun refreshLoadsNewBalances() {
        balancesViewModel.refresh()

        // Once when initializing the viewmodel and again when refreshing
        verify(balanceRepository, times(2)).loadBalances()
    }
}