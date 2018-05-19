package com.zredna.bitfolio.account

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "balances")
data class Balance(
        @PrimaryKey
        val currency: String,
        var balance: Double
)