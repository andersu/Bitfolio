package com.zredna.bitfolio.db.datamodel

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "balances")
data class BalanceInBtc(
        @PrimaryKey
        val currency: String,
        var balanceInBtc: Double
)