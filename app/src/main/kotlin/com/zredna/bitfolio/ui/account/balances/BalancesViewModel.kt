package com.zredna.bitfolio.ui.account.balances

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zredna.bitfolio.db.datamodel.BalanceInBtc
import com.zredna.bitfolio.domain.GetBalancesUseCase
import com.zredna.bitfolio.extensions.roundTo8
import com.zredna.bitfolio.repository.Resource

class BalancesViewModel(private val getBalances: GetBalancesUseCase) : ViewModel() {

    private val _balances = MediatorLiveData<Resource<List<BalanceInBtc>>>()
    val balances: LiveData<Resource<List<BalanceInBtc>>>
        get() = _balances

    var totalBalance: LiveData<Double> = Transformations.map(balances) { resource ->
        resource.data?.sumByDouble { it.balanceInBtc }?.roundTo8()
    }

    init {
        postLoading()
        _balances.addSource(getBalances()) {
            _balances.value = it
        }
    }

    fun refresh() {
        postLoading()
        getBalances()
    }

    private fun postLoading() {
        _balances.value = Resource.loading(null)
    }
}