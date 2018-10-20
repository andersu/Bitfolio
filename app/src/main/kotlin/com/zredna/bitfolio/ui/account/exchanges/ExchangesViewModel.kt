package com.zredna.bitfolio.ui.account.exchanges

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zredna.bitfolio.model.ExchangeCredentials
import com.zredna.bitfolio.repository.ExchangeRepository

class ExchangesViewModel(
        private val exchangeRepository: ExchangeRepository
) : ViewModel() {

    var exchangeCredentials: LiveData<List<ExchangeCredentials>> =
            Transformations.map(exchangeRepository.loadExchanges()) { exchanges ->
                exchanges.map {
                    exchangeRepository.getCredentialsForExchange(it.name)
                }
            }

    var exchanges = exchangeRepository.loadExchanges()
}