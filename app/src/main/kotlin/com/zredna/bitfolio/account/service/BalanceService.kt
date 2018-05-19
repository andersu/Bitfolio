package com.zredna.bitfolio.account.service

import com.zredna.bitfolio.account.Balance

interface BalanceService {
    fun getBalances(onSuccess: (List<Balance>) -> Unit, onFailure: () -> Unit)
}