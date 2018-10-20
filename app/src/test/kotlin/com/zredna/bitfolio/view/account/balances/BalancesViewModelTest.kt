package com.zredna.bitfolio.view.account.balances

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.zredna.bitfolio.BaseLiveDataTest
import com.zredna.bitfolio.db.datamodel.BalanceInBtc
import com.zredna.bitfolio.domain.GetBalancesUseCase
import com.zredna.bitfolio.repository.Resource
import com.zredna.bitfolio.ui.account.balances.BalancesViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class BalancesViewModelTest : BaseLiveDataTest() {

    private lateinit var balancesViewModel: BalancesViewModel

    private val balancesLiveData = MutableLiveData<Resource<List<BalanceInBtc>>>()

    @Mock
    private lateinit var getBalances: GetBalancesUseCase

    @Mock
    private lateinit var balancesObserver: Observer<Resource<List<BalanceInBtc>>>

    @Mock
    private lateinit var totalBalanceObserver: Observer<Double?>

    @Before
    fun setUp() {
        given(getBalances()).willReturn(balancesLiveData)

        balancesViewModel = BalancesViewModel(getBalances)
    }

    @Test
    fun initGetsBalances() {
        verify(getBalances).invoke()
    }

    @Test
    fun initTriggersLoadingState() {
        balancesViewModel.balances.observeForever(balancesObserver)
        verify(balancesObserver).onChanged(Resource.loading(null))
    }

    @Test
    fun emptyBalancesTriggersBalancesObserver() {
        balancesViewModel.balances.observeForever(balancesObserver)

        val balancesResource = Resource.success((emptyList<BalanceInBtc>()))
        balancesLiveData.value = balancesResource

        assertEquals(balancesResource, balancesViewModel.balances.value)
    }

    @Test
    fun emptyBalancesTriggersTotalBalanceObserver() {
        balancesViewModel.totalBalance.observeForever(totalBalanceObserver)

        val balancesResource = Resource.success((emptyList<BalanceInBtc>()))
        balancesLiveData.value = balancesResource

        assertEquals(0.0, balancesViewModel.totalBalance.value)
    }

    @Test
    fun nonEmptyBalancesTriggersBalancesObserver() {
        balancesViewModel.balances.observeForever(balancesObserver)
        val balances = Resource.success(
                listOf(BalanceInBtc("currency1", 0.10),
                        BalanceInBtc("currency2", 0.20)
                )
        )
        balancesLiveData.value = balances

        assertEquals(balances, balancesViewModel.balances.value)
    }

    @Test
    fun nonEmptyBalancesTriggersTotalBalanceObserver() {
        balancesViewModel.totalBalance.observeForever(totalBalanceObserver)
        val balances = Resource.success(
                listOf(BalanceInBtc("currency1", 0.10),
                        BalanceInBtc("currency2", 0.20)
                )
        )
        balancesLiveData.value = balances

        assertEquals(0.30, balancesViewModel.totalBalance.value)
    }

    @Test
    fun refreshTriggersLoadingState() {
        balancesViewModel.balances.observeForever(balancesObserver)

        balancesViewModel.refresh()

        // Once when initializing the view model and again when refreshing
        verify(balancesObserver, times(2)).onChanged(Resource.loading(null))
    }

    @Test
    fun refreshLoadsNewBalances() {
        balancesViewModel.refresh()

        // Once when initializing the view model and again when refreshing
        verify(getBalances, times(2)).invoke()
    }
}