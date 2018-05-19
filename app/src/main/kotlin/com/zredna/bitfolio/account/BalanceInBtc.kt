package com.zredna.bitfolio.account

import java.math.BigDecimal

data class BalanceInBtc(
        val currency: String,
        var balanceInBtc: Double
) {
    init {
        balanceInBtc = balanceInBtc.roundTo8()
    }
}