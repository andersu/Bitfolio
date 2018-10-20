package com.zredna.bitfolio.repository

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.zredna.bitfolio.BaseLiveDataTest
import com.zredna.bitfolio.db.ExchangeDao
import com.zredna.bitfolio.db.datamodel.Exchange
import com.zredna.bitfolio.domain.model.ExchangeCredentials
import com.zredna.bitfolio.domain.model.ExchangeName
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ExchangeRepositoryTest: BaseLiveDataTest() {
    private lateinit var exchangeRepository: ExchangeRepository

    @Mock
    private lateinit var exchangeDao: ExchangeDao
    @Mock
    private lateinit var sharedPreferences: SharedPreferences
    @Mock
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor
    @Mock
    private lateinit var exchangeCredentials: ExchangeCredentials

    @Before
    fun setUp() {
        exchangeRepository = ExchangeRepository(exchangeDao, sharedPreferences)
    }

    private fun setUpSaveExchangeTest(
            exchangeName: ExchangeName = ExchangeName.BITTREX,
            apiKey: String = "apiKey",
            secret: String = "secret"
    ) {
        given(exchangeCredentials.name).willReturn(exchangeName)
        given(exchangeCredentials.apiKey).willReturn(apiKey)
        given(exchangeCredentials.secret).willReturn(secret)
        given(sharedPreferences.edit()).willReturn(sharedPreferencesEditor)
        exchangeRepository = ExchangeRepository(exchangeDao, sharedPreferences)
    }

    @Test
    fun saveExchangeInsertsExchangeUsingDao() {
        val exchangeName = ExchangeName.BITTREX
        setUpSaveExchangeTest(exchangeName = exchangeName)

        exchangeRepository.saveExchange(exchangeCredentials)

        verify(exchangeDao).insert(Exchange(exchangeName.name))
    }

    @Test
    fun saveExchangeSavesApiKeyAndSecretInPreferences() {
        val apiKey = "apiKey"
        val name = ExchangeName.BITTREX
        val secret = "secret"
        setUpSaveExchangeTest(name, apiKey, secret)

        exchangeRepository.saveExchange(exchangeCredentials)

        verify(sharedPreferencesEditor).putString("${name}_api_key", apiKey)
        verify(sharedPreferencesEditor).putString("${name}_secret", secret)
        verify(sharedPreferencesEditor).apply()
    }

    @Test
    fun loadExchangesReturnsExchangesLiveDataFromDao() {
        val exchangesLiveData = MutableLiveData<List<Exchange>>()
        val exchanges = emptyList<Exchange>()
        exchangesLiveData.value = exchanges
        given(exchangeDao.getExchanges()).willReturn(exchangesLiveData)
        exchangeRepository = ExchangeRepository(exchangeDao, sharedPreferences)

        val result = exchangeRepository.loadExchanges()

        assertEquals(exchangesLiveData, result)
    }

    @Test
    fun getCredentialsForExchangeReturnsExchangeCredentialsWithValuesFromSharedPreferences() {
        val exchangeName = ExchangeName.BINANCE
        val apiKey = "apiKey"
        val secret = "secret"
        given(sharedPreferences.getString("${exchangeName}_api_key", ""))
                .willReturn(apiKey)
        given(sharedPreferences.getString("${exchangeName}_secret", ""))
                .willReturn(secret)

        exchangeRepository = ExchangeRepository(exchangeDao, sharedPreferences)

        val exchangeCredentials = exchangeRepository.getCredentialsForExchange(exchangeName)

        assertEquals(exchangeName, exchangeCredentials.name)
        assertEquals(apiKey, exchangeCredentials.apiKey)
        assertEquals(secret, exchangeCredentials.secret)
    }

    @Test
    fun containsCredentialsForExchangeReturnsFalseIfNoApiKeyInSharedPreferences() {
        val exchangeName = ExchangeName.BINANCE
        val secret = "secret"
        given(sharedPreferences.getString("${exchangeName}_api_key", ""))
                .willReturn("")
        given(sharedPreferences.getString("${exchangeName}_secret", ""))
                .willReturn(secret)
        exchangeRepository = ExchangeRepository(exchangeDao, sharedPreferences)

        val result = exchangeRepository.containsCredentialsForExchange(exchangeName)

        assertFalse(result)
    }

    @Test
    fun containsCredentialsForExchangeReturnsTrueIfApiKeyInSharedPreferences() {
        val exchangeName = ExchangeName.BITTREX
        val apiKey = "apiKey"
        given(sharedPreferences.getString("${exchangeName}_api_key", ""))
                .willReturn(apiKey)
        given(sharedPreferences.getString("${exchangeName}_secret", ""))
                .willReturn("")
        exchangeRepository = ExchangeRepository(exchangeDao, sharedPreferences)

        val result = exchangeRepository.containsCredentialsForExchange(exchangeName)

        assertTrue(result)
    }

    @Test
    fun deleteRemovesApiKeyAndSecretFromSharedPreferences() {
        val exchangeCredentials = mock(ExchangeCredentials::class.java)
        val exchangeName = ExchangeName.BITTREX

        given(exchangeCredentials.name).willReturn(exchangeName)
        given(sharedPreferences.edit()).willReturn(sharedPreferencesEditor)

        exchangeRepository.delete(exchangeCredentials)

        verify(sharedPreferencesEditor).remove("${exchangeName.name}_api_key")
        verify(sharedPreferencesEditor).remove("${exchangeName.name}_secret")
        verify(sharedPreferencesEditor).apply()
    }

    @Test
    fun deleteCallsDaoToDeleteExchange() {
        val exchangeCredentials = mock(ExchangeCredentials::class.java)
        val exchangeName = ExchangeName.BITTREX

        given(exchangeCredentials.name).willReturn(exchangeName)
        given(sharedPreferences.edit()).willReturn(sharedPreferencesEditor)

        exchangeRepository.delete(exchangeCredentials)

        verify(exchangeDao).delete(Exchange(exchangeName.name))
    }
}