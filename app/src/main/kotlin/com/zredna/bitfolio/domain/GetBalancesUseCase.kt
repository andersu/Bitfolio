package com.zredna.bitfolio.domain

import androidx.lifecycle.LiveData
import com.zredna.bitfolio.db.datamodel.BalanceInBtc
import com.zredna.bitfolio.repository.BalanceRepository
import com.zredna.bitfolio.repository.Resource

class GetBalancesUseCase(
        private val balanceRepository: BalanceRepository
) {
    operator fun invoke(): LiveData<Resource<List<BalanceInBtc>>> {
        return balanceRepository.loadBalances()
    }
}