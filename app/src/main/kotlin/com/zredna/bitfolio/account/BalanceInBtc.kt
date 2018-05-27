package com.zredna.bitfolio.account

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "balances")
data class BalanceInBtc(
        @PrimaryKey
        val currency: String,
        var balanceInBtc: Double
)