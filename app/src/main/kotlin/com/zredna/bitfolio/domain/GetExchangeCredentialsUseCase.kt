package com.zredna.bitfolio.domain

import com.zredna.bitfolio.domain.model.ExchangeCredentials
import com.zredna.bitfolio.domain.model.ExchangeName
import com.zredna.bitfolio.repository.ExchangeRepository

class GetExchangeCredentialsUseCase(private val exchangeRepository: ExchangeRepository) {
    operator fun invoke(exchangeName: ExchangeName): ExchangeCredentials {
        return exchangeRepository.getCredentialsForExchange(exchangeName)
    }
}