package com.zredna.bitfolio.view.account.balances

import androidx.lifecycle.MutableLiveData
import com.zredna.bitfolio.BaseLiveDataTest
import com.zredna.bitfolio.db.datamodel.BalanceInBtc
import com.zredna.bitfolio.repository.BalanceRepository
import com.zredna.bitfolio.repository.Resource
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class BalancesViewModelTest : BaseLiveDataTest() {

    private lateinit var balancesViewModel: BalancesViewModel

    @Mock
    private lateinit var balanceRepository: BalanceRepository

    @Before
    fun setUp() {
        balancesViewModel = BalancesViewModel(balanceRepository)
    }

    @Test
    fun getBalancesEmptyList() {
        val balancesLiveData = MutableLiveData<Resource<List<BalanceInBtc>>>()
        val balances = Resource.success((emptyList<BalanceInBtc>()))
        given(balanceRepository.loadBalances()).willReturn(balancesLiveData)

        balancesViewModel = BalancesViewModel(balanceRepository)
        balancesLiveData.value = balances

        balancesViewModel.balances.observeForever { assertEquals(it, balances) }
        balancesViewModel.totalBalance.observeForever { assertEquals(0.0, it) }
    }

    @Test
    fun getBalancesNonEmptyList() {
        val balancesLiveData = MutableLiveData<Resource<List<BalanceInBtc>>>()
        val balances = Resource.success(
                listOf(BalanceInBtc("currency1", 0.10),
                        BalanceInBtc("currency2", 0.20)
                )
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