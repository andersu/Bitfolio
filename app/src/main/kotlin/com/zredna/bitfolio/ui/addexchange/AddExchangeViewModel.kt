package com.zredna.bitfolio.ui.addexchange

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zredna.bitfolio.ExchangeName
import com.zredna.bitfolio.model.ExchangeCredentials
import com.zredna.bitfolio.repository.ExchangeRepository

class AddExchangeViewModel(
        private val exchangeRepository: ExchangeRepository
): ViewModel() {
    private val selectedExchange = MutableLiveData<ExchangeName>()
    private val isAddExchangeEnabled = MutableLiveData<Boolean>()

    private var apiKey = ""
    private var secret = ""

    init {
        isAddExchangeEnabled.value = false
    }

    fun getSelectedExchange(): LiveData<ExchangeName> {
        return selectedExchange
    }

    fun isAddExchangeEnabled(): LiveData<Boolean> {
        return isAddExchangeEnabled
    }

    fun exchangeSelected(exchangeName: ExchangeName) {
        selectedExchange.value = exchangeName
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

    fun addExchange(exchangeName: ExchangeName, apiKey: String, secret: String) {
        exchangeRepository.saveExchange(ExchangeCredentials(exchangeName.name, apiKey, secret))
    }
}