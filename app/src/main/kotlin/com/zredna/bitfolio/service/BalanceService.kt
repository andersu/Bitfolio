package com.zredna.bitfolio.service

import com.zredna.bitfolio.model.Balance

interface BalanceService {
    fun getBalances(onSuccess: (List<Balance>) -> Unit, onFailure: () -> Unit)
}