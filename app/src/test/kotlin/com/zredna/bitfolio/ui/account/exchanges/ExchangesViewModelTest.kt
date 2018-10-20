package com.zredna.bitfolio.ui.account.exchanges

import androidx.lifecycle.MutableLiveData
import com.zredna.bitfolio.BaseLiveDataTest
import com.zredna.bitfolio.db.datamodel.Exchange
import com.zredna.bitfolio.model.ExchangeCredentials
import com.zredna.bitfolio.repository.ExchangeRepository
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.mock

class ExchangesViewModelTest: BaseLiveDataTest() {

    private lateinit var exchangesViewModel: ExchangesViewModel

    @Mock
    private lateinit var exchangeRepository: ExchangeRepository

    @Before
    fun setUp() {
        exchangesViewModel = ExchangesViewModel(exchangeRepository)
    }

    @Test
    fun getExchangeCredentialsEmptyList() {
        val exchangesLiveData = MutableLiveData<List<Exchange>>()
        val exchanges = emptyList<Exchange>()
        exchangesLiveData.value = exchanges
        given(exchangeRepository.loadExchanges()).willReturn(exchangesLiveData)

        exchangesViewModel = ExchangesViewModel(exchangeRepository)

        exchangesViewModel.exchangeCredentials.observeForever {
            assert(it!!.isEmpty())
        }
    }

    @Test
    fun getExchangeCredentials() {
        val exchange1Name = "Exchange 1"
        val exchange2Name = "Exchange 2"
        val exchange1Credentials = mock(ExchangeCredentials::class.java)
        val exchange2Credentials = mock(ExchangeCredentials::class.java)
        val exchangesLiveData = MutableLiveData<List<Exchange>>()
        val exchanges = listOf(Exchange(exchange1Name), Exchange(exchange2Name))
        exchangesLiveData.value = exchanges

        given(exchangeRepository.loadExchanges()).willReturn(exchangesLiveData)
        given(exchangeRepository.getCredentialsForExchange(exchange1Name))
                .willReturn(exchange1Credentials)
        given(exchangeRepository.getCredentialsForExchange(exchange2Name))
                .willReturn(exchange2Credentials)

        exchangesViewModel = ExchangesViewModel(exchangeRepository)

        exchangesViewModel.exchangeCredentials.observeForever {
            assertEquals(it?.first(), exchange1Credentials)
            assertEquals(it?.last(), exchange2Credentials)
        }
    }

    @Test
    fun getExchanges() {
        val exchangesLiveData = MutableLiveData<List<Exchange>>()
        val exchanges = listOf(mock(Exchange::class.java), mock(Exchange::class.java))
        exchangesLiveData.value = exchanges
        given(exchangeRepository.loadExchanges()).willReturn(exchangesLiveData)

        exchangesViewModel = ExchangesViewModel(exchangeRepository)

        exchangesViewModel.exchanges.observeForever {
            assertEquals(exchanges, it)
        }
    }
}