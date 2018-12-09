package com.zredna.bitfolio.domain

import com.zredna.bitfolio.repository.BalanceRepository

class DeleteBalancesUseCase(
        private val balanceRepository: BalanceRepository
) {
    operator fun invoke() {
        balanceRepository.deleteBalances()
    }
}