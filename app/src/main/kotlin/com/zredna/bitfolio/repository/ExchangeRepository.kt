package com.zredna.bitfolio.repository

import androidx.lifecycle.LiveData
import android.content.SharedPreferences
import com.zredna.bitfolio.db.ExchangeDao
import com.zredna.bitfolio.db.datamodel.Exchange
import com.zredna.bitfolio.model.ExchangeCredentials
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ExchangeRepository(
        private val exchangeDao: ExchangeDao,
        private val sharedPreferences: SharedPreferences
) {
    private val exchanges = exchangeDao.getExchanges()

    private fun apiKeyPreferenceKey(exchangeName: String) = "${exchangeName}_api_key"
    private fun secretPreferenceKey(exchangeName: String) = "${exchangeName}_secret"

    fun saveExchange(exchangeCredentials: ExchangeCredentials) {
        Single.just(exchangeCredentials)
                .subscribeOn(Schedulers.io())
                .doOnSuccess {
                    exchangeDao.insert(Exchange(exchangeCredentials.name))
                }
                .subscribe()

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

    fun loadExchanges(): LiveData<List<Exchange>> {
        return exchanges
    }

    fun getCredentialsForExchange(exchangeName: String): ExchangeCredentials {
        val apiKey = sharedPreferences.getString(apiKeyPreferenceKey(exchangeName), "")
        val secret = sharedPreferences.getString(secretPreferenceKey(exchangeName), "")

        return ExchangeCredentials(exchangeName, apiKey, secret)
    }

    fun containsCredentialsForExchange(exchangeName: String): Boolean {
        return getCredentialsForExchange(exchangeName).apiKey.isNotEmpty()
    }

    fun delete(exchangeCredentials: ExchangeCredentials) {
        val exchangeName = exchangeCredentials.name
        Single.just(exchangeCredentials)
                .subscribeOn(Schedulers.io())
                .doOnSuccess {
                    val editor = sharedPreferences.edit()
                    editor.remove(apiKeyPreferenceKey(exchangeName))
                    editor.remove(secretPreferenceKey(exchangeName))
                    editor.apply()
                    exchangeDao.delete(Exchange(exchangeName))
                }
                .subscribe()
    }
}