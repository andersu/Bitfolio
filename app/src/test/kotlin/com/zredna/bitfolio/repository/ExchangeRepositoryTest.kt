package com.zredna.bitfolio.repository

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.zredna.bitfolio.BaseTest
import com.zredna.bitfolio.db.ExchangeDao
import com.zredna.bitfolio.db.datamodel.Exchange
import com.zredna.bitfolio.model.ExchangeCredentials
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify

class ExchangeRepositoryTest: BaseTest() {
    private lateinit var exchangeRepository: ExchangeRepository

    @Mock
    private lateinit var exchangeDao: ExchangeDao
    @Mock
    private lateinit var sharedPreferences: SharedPreferences
    @Mock
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor
    @Mock
    private lateinit var exchangeCredentials: ExchangeCredentials

    /*
     * This is required to test LiveData.
     * When setting values Android checks to see what thread the call is made from,
     * and this rule returns the required result to avoid an NPE
     */
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun saveExchangeInsertsExchangeUsingDao() {
        setUpSaveExchangeTest()

        exchangeRepository.saveExchange(exchangeCredentials)

        verify(exchangeDao).insert(anyKotlin())
    }

    @Test
    fun saveExchangeSavesApiKeyAndSecretInPreferences() {
        val apiKey = "apiKey"
        val name = "name"
        val secret = "secret"
        setUpSaveExchangeTest(name, apiKey, secret)

        exchangeRepository.saveExchange(exchangeCredentials)

        verify(sharedPreferencesEditor).putString("${name}_api_key", apiKey)
        verify(sharedPreferencesEditor).putString("${name}_secret", secret)
        verify(sharedPreferencesEditor).apply()
    }

    private fun setUpSaveExchangeTest(
            name: String = "name",
            apiKey: String = "apiKey",
            secret: String = "secret"
    ) {
        given(exchangeCredentials.name).willReturn(name)
        given(exchangeCredentials.apiKey).willReturn(apiKey)
        given(exchangeCredentials.secret).willReturn(secret)
        given(sharedPreferences.edit()).willReturn(sharedPreferencesEditor)
        exchangeRepository = ExchangeRepository(exchangeDao, sharedPreferences)
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
        val exchangeName = "exchangeName"
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
        val exchangeName = "exchangeName"
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
        val exchangeName = "exchangeName"
        val apiKey = "apiKey"
        given(sharedPreferences.getString("${exchangeName}_api_key", ""))
                .willReturn(apiKey)
        given(sharedPreferences.getString("${exchangeName}_secret", ""))
                .willReturn("")
        exchangeRepository = ExchangeRepository(exchangeDao, sharedPreferences)

        val result = exchangeRepository.containsCredentialsForExchange(exchangeName)

        assertTrue(result)
    }
}