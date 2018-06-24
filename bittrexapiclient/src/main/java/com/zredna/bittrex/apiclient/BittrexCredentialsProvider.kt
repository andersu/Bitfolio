package com.zredna.bittrex.apiclient

interface BittrexCredentialsProvider {
    fun getCredentials(): BittrexCredentials
}