package com.zredna.bitfolio.account.view.account

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.zredna.bitfolio.account.BalanceInBtc
import com.zredna.bitfolio.account.repository.BalanceRepository
import com.zredna.bitfolio.account.roundTo8

class AccountViewModel(
        private val balanceRepository: BalanceRepository
) : ViewModel() {

    private val isRefreshing = MutableLiveData<Boolean>()

    var balances: LiveData<List<BalanceInBtc>> = balanceRepository.loadBalances()
    var totalBalance: LiveData<Double> = Transformations.map(
            balances,
            { it.sumByDouble { it.balanceInBtc }.roundTo8() }
    )

    fun isRefreshing(): LiveData<Boolean> = isRefreshing

    fun balancesUpdated() {
         isRefreshing.value = false
    }

    fun refresh() {
        isRefreshing.value = true
        balanceRepository.loadBalances()
    }
}