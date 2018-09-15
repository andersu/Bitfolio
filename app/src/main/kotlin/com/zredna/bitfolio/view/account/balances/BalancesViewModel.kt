package com.zredna.bitfolio.view.account.balances

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.zredna.bitfolio.repository.BalanceRepository
import com.zredna.bitfolio.roundTo8

class BalancesViewModel(
        private val balanceRepository: BalanceRepository
) : ViewModel() {

    private val isRefreshing = MutableLiveData<Boolean>()

    var balances = balanceRepository.loadBalances()
    var totalBalance: LiveData<Double> = Transformations.map(balances) { balancesInBtc ->
        balancesInBtc.sumByDouble { it.balanceInBtc }.roundTo8()
    }

    fun isRefreshing(): LiveData<Boolean> = isRefreshing

    fun balancesUpdated() {
         isRefreshing.value = false
    }

    fun refresh() {
        isRefreshing.value = true
        balanceRepository.loadBalances()
    }
}