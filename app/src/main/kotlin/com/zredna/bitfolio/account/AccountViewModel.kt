package com.zredna.bitfolio.account

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class AccountViewModel(
        private val balanceRepository: BalanceRepository
): ViewModel() {

    var balances: MutableLiveData<List<BalanceInBtc>> = balanceRepository.loadBalances()

    fun refresh() {
        balanceRepository.refreshBalances {
            balances.postValue(it)
        }
    }
}