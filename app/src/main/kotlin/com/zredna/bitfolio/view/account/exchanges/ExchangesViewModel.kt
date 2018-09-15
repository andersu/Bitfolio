package com.zredna.bitfolio.view.account.exchanges

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
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