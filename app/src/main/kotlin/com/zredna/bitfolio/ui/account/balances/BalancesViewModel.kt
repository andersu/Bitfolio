package com.zredna.bitfolio.ui.account.balances

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zredna.bitfolio.db.datamodel.BalanceInBtc
import com.zredna.bitfolio.repository.BalanceRepository
import com.zredna.bitfolio.repository.Resource
import com.zredna.bitfolio.roundTo8

class BalancesViewModel(
        private val balanceRepository: BalanceRepository
) : ViewModel() {

    private val mutableBalances = MediatorLiveData<Resource<List<BalanceInBtc>>>()
    val balances: LiveData<Resource<List<BalanceInBtc>>>
        get() = mutableBalances

    var totalBalance: LiveData<Double> = Transformations.map(balances) { resource ->
        resource.data?.sumByDouble { it.balanceInBtc }?.roundTo8()
    }

    init {
        postLoading()
        mutableBalances.addSource(balanceRepository.loadBalances()) {
            mutableBalances.value = it
        }
    }

    fun refresh() {
        postLoading()
        balanceRepository.loadBalances()
    }

    private fun postLoading() {
        mutableBalances.value = Resource.loading(null)
    }
}