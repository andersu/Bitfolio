package com.zredna.bitfolio.domain

import androidx.lifecycle.LiveData
import com.zredna.bitfolio.db.datamodel.Exchange
import com.zredna.bitfolio.repository.ExchangeRepository

class GetExchangesUseCase(private val exchangeRepository: ExchangeRepository) {
    operator fun invoke(): LiveData<List<Exchange>> {
        return exchangeRepository.loadExchanges()
    }
}