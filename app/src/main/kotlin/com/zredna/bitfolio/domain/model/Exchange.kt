package com.zredna.bitfolio.domain.model

data class ExchangeCredentials(
        val name: ExchangeName,
        val apiKey: String,
        val secret: String
)