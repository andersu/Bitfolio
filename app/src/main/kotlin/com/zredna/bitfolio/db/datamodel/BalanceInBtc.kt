package com.zredna.bitfolio.db.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "balances")
data class BalanceInBtc(
        @PrimaryKey
        val currency: String,
        var balanceInBtc: Double
)