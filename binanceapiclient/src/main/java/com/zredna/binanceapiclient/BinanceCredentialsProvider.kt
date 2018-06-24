package com.zredna.binanceapiclient

interface BinanceCredentialsProvider {
    fun getCredentials(): BinanceCredentials
}