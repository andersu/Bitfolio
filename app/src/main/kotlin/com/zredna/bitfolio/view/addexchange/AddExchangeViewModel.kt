package com.zredna.bitfolio.view.addexchange

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.zredna.bitfolio.Exchange
import com.zredna.bitfolio.ExchangeCredentials
import com.zredna.bitfolio.repository.ExchangeCredentialsRepository

class AddExchangeViewModel(
        private val exchangeCredentialsRepository: ExchangeCredentialsRepository
): ViewModel() {
    private val selectedExchange = MutableLiveData<Exchange>()
    private val isAddExchangeEnabled = MutableLiveData<Boolean>()

    private var apiKey = ""
    private var secret = ""

    init {
        isAddExchangeEnabled.value = false
    }

    fun getSelectedExchange(): LiveData<Exchange> {
        return selectedExchange
    }

    fun isAddExchangeEnabled(): LiveData<Boolean> {
        return isAddExchangeEnabled
    }

    fun exchangeSelected(exchange: Exchange) {
        selectedExchange.value = exchange
    }

    fun apiKeyTextChanged(text: String) {
        apiKey = text
        updateAddExchangeEnabled()
    }

    fun secretTextChanged(text: String) {
        secret = text
        updateAddExchangeEnabled()
    }

    private fun updateAddExchangeEnabled() {
        isAddExchangeEnabled.value = apiKey.isNotEmpty() && secret.isNotEmpty()
    }

    fun addExchange(exchange: Exchange, apiKey: String, secret: String) {
        exchangeCredentialsRepository.saveExchangeCredentials(
                ExchangeCredentials(exchange, apiKey, secret)
        )
    }
}