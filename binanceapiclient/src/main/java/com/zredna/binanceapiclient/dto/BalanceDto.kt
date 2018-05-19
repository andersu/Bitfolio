package com.zredna.binanceapiclient.dto

data class BalanceDto(
        val asset: String,
        val free: String,
        val locked: String
)