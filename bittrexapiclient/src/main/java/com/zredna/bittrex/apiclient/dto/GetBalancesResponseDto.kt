package com.zredna.bittrex.apiclient.dto

data class GetBalancesResponseDto(
        var success: Boolean,
        var message: String?,
        var result: List<BalanceDto>?
)