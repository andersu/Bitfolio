package com.zredna.bitfolio.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zredna.bitfolio.db.datamodel.BalanceInBtc
import com.zredna.bitfolio.db.datamodel.Exchange

@Database(
        entities = [BalanceInBtc::class, Exchange::class],
        version = 1,
        exportSchema = true
)
abstract class BitfolioDatabase : RoomDatabase() {
    abstract fun balanceDao(): BalanceDao
    abstract fun exchangeDao(): ExchangeDao
}