package com.zredna.bitfolio.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.zredna.bitfolio.BalanceInBtc

@Database(entities = [BalanceInBtc::class], version = 1, exportSchema = true)
abstract class BitfolioDatabase : RoomDatabase() {
    abstract fun balanceDao(): BalanceDao
}