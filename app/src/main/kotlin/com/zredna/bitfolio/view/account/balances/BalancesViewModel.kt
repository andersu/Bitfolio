package com.zredna.bitfolio.view.account.balances

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.zredna.bitfolio.db.datamodel.BalanceInBtc
import com.zredna.bitfolio.repository.BalanceRepository
import com.zredna.bitfolio.repository.Resource
import com.zredna.bitfolio.roundTo8

class BalancesViewModel(
        private val balanceRepository: BalanceRepository
) : ViewModel() {

    private val isRefreshing = MutableLiveData<Boolean>()

    var balances: LiveData<Resource<List<BalanceInBtc>>> = balanceRepository.loadBalances()

    var totalBalance: LiveData<Double> = Transformations.map(balances) { resource ->
        resource.data?.sumByDouble { it.balanceInBtc }?.roundTo8() ?: 0.0
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