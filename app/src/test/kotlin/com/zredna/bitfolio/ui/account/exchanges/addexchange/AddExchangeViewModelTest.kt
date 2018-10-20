package com.zredna.bitfolio.ui.account.exchanges.addexchange

import androidx.lifecycle.Observer
import com.zredna.bitfolio.BaseLiveDataTest
import com.zredna.bitfolio.domain.model.ExchangeName
import com.zredna.bitfolio.domain.SaveExchangeUseCase
import com.zredna.bitfolio.domain.model.ExchangeCredentials
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify

class AddExchangeViewModelTest : BaseLiveDataTest() {

    @Mock
    private lateinit var saveExchange: SaveExchangeUseCase

    @Mock
    private lateinit var selectedExchangeObserver: Observer<ExchangeName>

    @Mock
    private lateinit var isAddExchangeEnabledObserver: Observer<Boolean>

    private lateinit var viewModel: AddExchangeViewModel

    @Before
    fun setUp() {
        viewModel = AddExchangeViewModel(saveExchange)

        viewModel.selectedExchange.observeForever(selectedExchangeObserver)
        viewModel.isAddExchangeEnabled.observeForever(isAddExchangeEnabledObserver)
    }

    @Test
    fun initSetsIsAddExchangeEnabledToFalse() {
        assertEquals(false, viewModel.isAddExchangeEnabled.value)
    }

    @Test
    fun exchangeSelectedUpdatesSelectedExchange() {
        val exchangeName = ExchangeName.BITTREX

        viewModel.exchangeSelected(exchangeName)

        assertEquals(exchangeName, viewModel.selectedExchange.value)
    }

    @Test
    fun apiKeyTextChangedToNonEmptyStringAloneSetsIsAddExchangeEnabledToFalse() {
        val apiKey = "apiKey"

        viewModel.apiKeyTextChanged(apiKey)

        assertEquals(false, viewModel.isAddExchangeEnabled.value)
    }

    @Test
    fun secretTextChangedToNonEmptyStringAloneSetsIsAddExchangeEnabledToFalse() {
        val secret = "secret"

        viewModel.secretTextChanged(secret)

        assertEquals(false, viewModel.isAddExchangeEnabled.value)
    }

    @Test
    fun apiAndSecretTextChangedToNonEmptyStringsSetsIsAddExchangeEnabledToTrue() {
        val apiKey = "apiKey"
        val secret = "secret"

        viewModel.apiKeyTextChanged(apiKey)
        viewModel.secretTextChanged(secret)

        assertEquals(true, viewModel.isAddExchangeEnabled.value)
    }

    @Test
    fun addExchangeCallsSaveExchangeUseCase() {
        val exchangeName = ExchangeName.BINANCE
        val apiKey = "apiKey"
        val secret = "secret"

        viewModel.addExchange(exchangeName, apiKey, secret)

        verify(saveExchange).invoke(ExchangeCredentials(exchangeName, apiKey, secret))
    }
}