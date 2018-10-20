package com.zredna.bitfolio.domain

import com.zredna.bitfolio.domain.model.ExchangeCredentials
import com.zredna.bitfolio.repository.ExchangeRepository

class DeleteExchangeUseCase(
        private val exchangeRepository: ExchangeRepository
) {
    operator fun invoke(exchangeCredentials: ExchangeCredentials) {
        exchangeRepository.delete(exchangeCredentials)
    }
}