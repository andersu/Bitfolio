package com.zredna.bitfolio.repository

import android.arch.lifecycle.LiveData
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

    fun saveExchange(exchangeCredentials: ExchangeCredentials) {
        Single.just(exchangeCredentials)
                .subscribeOn(Schedulers.io())
                .doOnSuccess {
                    exchangeDao.insert(Exchange(exchangeCredentials.name))
                }
                .subscribe()

        val editor = sharedPreferences.edit()
        editor.putString(
                "${exchangeCredentials.name}_api_key",
                exchangeCredentials.apiKey
        )
        editor.putString(
                "${exchangeCredentials.name}_secret",
                exchangeCredentials.secret
        )

        editor.apply()
    }

    fun loadExchanges(): LiveData<List<Exchange>> {
        return exchanges
    }

    fun getCredentialsForExchange(exchangeName: String): ExchangeCredentials {
        val apiKey = sharedPreferences.getString("${exchangeName}_api_key", "")
        val secret = sharedPreferences.getString("${exchangeName}_secret", "")

        return ExchangeCredentials(exchangeName, apiKey, secret)
    }

    fun containsCredentialsForExchange(exchangeName: String): Boolean {
        return getCredentialsForExchange(exchangeName).apiKey.isNotEmpty()
    }
}