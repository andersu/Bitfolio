package com.zredna.bitfolio.ui.addexchange

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zredna.bitfolio.ExchangeName
import com.zredna.bitfolio.domain.SaveExchangeUseCase
import com.zredna.bitfolio.model.ExchangeCredentials

class AddExchangeViewModel(
        private val saveExchange: SaveExchangeUseCase
): ViewModel() {

    private val _selectedExchange = MutableLiveData<ExchangeName>()
    val selectedExchange: LiveData<ExchangeName>
        get() = _selectedExchange

    private val _isAddExchangeEnabled = MutableLiveData<Boolean>()
    val isAddExchangeEnabled: LiveData<Boolean>
        get() = _isAddExchangeEnabled


    private var apiKey = ""
    private var secret = ""

    init {
        _isAddExchangeEnabled.value = false
    }

    fun exchangeSelected(exchangeName: ExchangeName) {
        _selectedExchange.value = exchangeName
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
        _isAddExchangeEnabled.value = apiKey.isNotEmpty() && secret.isNotEmpty()
    }

    fun addExchange(exchangeName: ExchangeName, apiKey: String, secret: String) {
        saveExchange(ExchangeCredentials(exchangeName.name, apiKey, secret))
    }
}