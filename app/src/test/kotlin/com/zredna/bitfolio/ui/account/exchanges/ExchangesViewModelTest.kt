package com.zredna.bitfolio.ui.account.exchanges

import androidx.lifecycle.MutableLiveData
import com.zredna.bitfolio.BaseLiveDataTest
import com.zredna.bitfolio.db.datamodel.Exchange
import com.zredna.bitfolio.domain.DeleteExchangeUseCase
import com.zredna.bitfolio.domain.GetExchangeCredentialsUseCase
import com.zredna.bitfolio.domain.GetExchangesUseCase
import com.zredna.bitfolio.domain.model.ExchangeCredentials
import com.zredna.bitfolio.domain.model.ExchangeName
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ExchangesViewModelTest: BaseLiveDataTest() {

    private lateinit var exchangesViewModel: ExchangesViewModel

    @Mock
    private lateinit var getExchanges: GetExchangesUseCase

    @Mock
    private lateinit var getExchangeCredentials: GetExchangeCredentialsUseCase

    @Mock
    private lateinit var deleteExchange: DeleteExchangeUseCase

    @Before
    fun setUp() {
        setUpExchangesViewModel()
    }

    private fun setUpExchangesViewModel() {
        exchangesViewModel = ExchangesViewModel(
                getExchanges,
                getExchangeCredentials,
                deleteExchange
        )
    }

    @Test
    fun getExchangeCredentialsEmptyList() {
        val exchangesLiveData = MutableLiveData<List<Exchange>>()
        val exchanges = emptyList<Exchange>()
        exchangesLiveData.value = exchanges
        given(getExchanges.invoke()).willReturn(exchangesLiveData)

        setUpExchangesViewModel()

        exchangesViewModel.exchangeCredentials.observeForever {
            assert(it!!.isEmpty())
        }
    }

    @Test
    fun getExchangeCredentials() {
        val exchange1Name = ExchangeName.BITTREX
        val exchange2Name = ExchangeName.BINANCE
        val exchange1Credentials = mock(ExchangeCredentials::class.java)
        val exchange2Credentials = mock(ExchangeCredentials::class.java)
        val exchangesLiveData = MutableLiveData<List<Exchange>>()
        val exchanges = listOf(Exchange(exchange1Name.name), Exchange(exchange2Name.name))
        exchangesLiveData.value = exchanges

        given(getExchanges.invoke()).willReturn(exchangesLiveData)
        given(getExchangeCredentials.invoke(exchange1Name))
                .willReturn(exchange1Credentials)
        given(getExchangeCredentials.invoke(exchange2Name))
                .willReturn(exchange2Credentials)

        setUpExchangesViewModel()

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
        given(getExchanges.invoke()).willReturn(exchangesLiveData)

        setUpExchangesViewModel()

        exchangesViewModel.exchanges.observeForever {
            assertEquals(exchanges, it)
        }
    }

    @Test
    fun deleteClickedCallsDeleteExchangeUseCase() {
        val exchangeCredentials = mock(ExchangeCredentials::class.java)
        exchangesViewModel.deleteClicked(exchangeCredentials)

        verify(deleteExchange).invoke(exchangeCredentials)
    }
}