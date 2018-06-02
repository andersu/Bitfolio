package com.zredna.bitfolio.repository

import android.content.SharedPreferences
import com.zredna.bitfolio.Exchange
import com.zredna.bitfolio.ExchangeCredentials

class ExchangeCredentialsRepository(
        private val sharedPreferences: SharedPreferences
) {
    fun saveExchangeCredentials(exchangeCredentials: ExchangeCredentials) {
        val editor = sharedPreferences.edit()
        editor.putString(
                "${exchangeCredentials.exchange.name}_api_key",
                exchangeCredentials.apiKey
        )
        editor.putString(
                "${exchangeCredentials.exchange.name}_secret",
                exchangeCredentials.secret
        )

        editor.apply()
    }

    fun getCredentialsForExchange(exchange: Exchange): ExchangeCredentials {
        val apiKey = sharedPreferences.getString("${exchange.name}_api_key", "")
        val secret = sharedPreferences.getString("${exchange.name}_secret", "")

        return ExchangeCredentials(exchange, apiKey, secret)
    }

    fun containsCredentialsForExchange(exchange: Exchange): Boolean {
        return getCredentialsForExchange(exchange).apiKey.isNotEmpty()
    }
}