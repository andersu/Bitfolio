package com.zredna.bitfolio.ui.account.exchanges

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zredna.bitfolio.domain.DeleteExchangeUseCase
import com.zredna.bitfolio.domain.GetExchangeCredentialsUseCase
import com.zredna.bitfolio.domain.GetExchangesUseCase
import com.zredna.bitfolio.domain.model.ExchangeCredentials
import com.zredna.bitfolio.domain.model.ExchangeName

class ExchangesViewModel(
        getExchanges: GetExchangesUseCase,
        private val getExchangeCredentials: GetExchangeCredentialsUseCase,
        private val deleteExchange: DeleteExchangeUseCase
) : ViewModel() {

    var exchangeCredentials: LiveData<List<ExchangeCredentials>> =
            Transformations.map(getExchanges()) { exchanges ->
                exchanges.map {
                    getExchangeCredentials(ExchangeName.valueOf(it.name))
                }
            }

    var exchanges = getExchanges()

    fun deleteClicked(exchangeCredentials: ExchangeCredentials) =
            deleteExchange(exchangeCredentials)
}