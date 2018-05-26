package com.zredna.bitfolio.account

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.zredna.bitfolio.account.repository.BalanceRepository

class AccountViewModel(
        private val balanceRepository: BalanceRepository
): ViewModel() {

    var balances: LiveData<List<BalanceInBtc>> = balanceRepository.loadBalances()
    var totalBalance: LiveData<Double> = Transformations.map(balances, {
        it.sumByDouble { it.balanceInBtc }.roundTo8()
    })

    fun refresh() {
        balanceRepository.loadBalances()
    }
}