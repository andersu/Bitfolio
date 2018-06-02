package com.zredna.bittrex.apiclient

interface CredentialsProvider {
    fun getCredentials(): BittrexCredentials
}