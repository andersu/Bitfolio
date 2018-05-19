package com.zredna.bittrex.apiclient.dto

import com.google.gson.annotations.SerializedName

data class BalanceDto(
        @SerializedName("Currency")
        val currency: String,

        @SerializedName("Balance")
        val balance: Double,

        @SerializedName("Available")
        val available: Double,

        @SerializedName("Pending")
        val pending: Double,

        @SerializedName("CryptoAddress")
        val cryptoAddress: String?,

        @SerializedName("Requested")
        val requested: Boolean,

        @SerializedName("Uuid")
        val uuid: String?
)