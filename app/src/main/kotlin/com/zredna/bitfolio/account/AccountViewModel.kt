package com.zredna.bitfolio.account

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.zredna.bitfolio.account.repository.BalanceRepository
import com.zredna.bitfolio.account.vo.Resource

class AccountViewModel(
        private val balanceRepository: BalanceRepository
): ViewModel() {

    var balances: LiveData<Resource<List<Balance>>> = balanceRepository.loadBalances()

    fun refresh() {
        // TODO
        /*balanceRepository.refreshBalances {
            balances.postValue(it)
        }*/
    }
}