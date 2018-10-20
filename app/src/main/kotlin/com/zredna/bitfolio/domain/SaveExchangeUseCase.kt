package com.zredna.bitfolio.domain

import com.zredna.bitfolio.model.ExchangeCredentials
import com.zredna.bitfolio.repository.ExchangeRepository

class SaveExchangeUseCase(
    private val exchangeRepository: ExchangeRepository
) {
    operator fun invoke(exchangeCredentials: ExchangeCredentials) {
        exchangeRepository.saveExchange(exchangeCredentials)
    }
}