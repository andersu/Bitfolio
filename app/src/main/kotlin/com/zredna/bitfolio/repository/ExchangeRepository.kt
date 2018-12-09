package com.zredna.bitfolio.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.zredna.bitfolio.db.ExchangeDao
import com.zredna.bitfolio.db.datamodel.Exchange
import com.zredna.bitfolio.domain.model.ExchangeCredentials
import com.zredna.bitfolio.domain.model.ExchangeName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ExchangeRepository(
        private val exchangeDao: ExchangeDao,
        private val sharedPreferences: SharedPreferences
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private val exchanges = exchangeDao.getExchanges()

    private fun apiKeyPreferenceKey(exchangeName: ExchangeName) = "${exchangeName.name}_api_key"
    private fun secretPreferenceKey(exchangeName: ExchangeName) = "${exchangeName.name}_secret"

    fun saveExchange(exchangeCredentials: ExchangeCredentials) {
        launch {
            exchangeDao.insert(Exchange(exchangeCredentials.name.name))
            val editor = sharedPreferences.edit()
            editor.putString(
                    apiKeyPreferenceKey(exchangeCredentials.name),
                    exchangeCredentials.apiKey
            )
            editor.putString(
                    secretPreferenceKey(exchangeCredentials.name),
                    exchangeCredentials.secret
            )
            editor.apply()
        }
    }

    fun loadExchanges(): LiveData<List<Exchange>> {
        return exchanges
    }

    fun getCredentialsForExchange(exchangeName: ExchangeName): ExchangeCredentials {
        val apiKey = sharedPreferences.getString(apiKeyPreferenceKey(exchangeName), "")
        val secret = sharedPreferences.getString(secretPreferenceKey(exchangeName), "")

        return ExchangeCredentials(exchangeName, apiKey, secret)
    }

    fun containsCredentialsForExchange(exchangeName: ExchangeName): Boolean {
        return getCredentialsForExchange(exchangeName).apiKey.isNotEmpty()
    }

    // TODO: Move to LocalDataSource as suspend fun
    fun delete(exchangeCredentials: ExchangeCredentials) {
        launch {
            val exchangeName = exchangeCredentials.name
            val editor = sharedPreferences.edit()
            editor.remove(apiKeyPreferenceKey(exchangeName))
            editor.remove(secretPreferenceKey(exchangeName))
            editor.apply()
            exchangeDao.delete(Exchange(exchangeName.name))
        }
    }
}