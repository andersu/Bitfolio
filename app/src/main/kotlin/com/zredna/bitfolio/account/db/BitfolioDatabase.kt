package com.zredna.bitfolio.account.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.zredna.bitfolio.account.Balance

@Database(entities = [Balance::class], version = 1, exportSchema = true)
abstract class BitfolioDatabase : RoomDatabase() {
    abstract fun balanceDao(): BalanceDao
}