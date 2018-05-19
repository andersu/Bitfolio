package com.zredna.bittrex.apiclient.dto

import com.google.gson.annotations.SerializedName

data class MarketSummaryDto(
        @SerializedName("MarketName")
        val marketName: String,

        @SerializedName("Last")
        val last: Double
)
