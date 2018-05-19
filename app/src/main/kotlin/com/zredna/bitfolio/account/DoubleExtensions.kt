package com.zredna.bitfolio.account

import java.math.BigDecimal

fun Double.roundTo8(): Double {
    return BigDecimal(this).setScale(8, BigDecimal.ROUND_HALF_UP).toDouble()
}