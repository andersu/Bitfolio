package com.zredna.bitfolio.account.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.zredna.bitfolio.account.Balance

@Dao
interface BalanceDao {

    @Query("SELECT * FROM balances")
    fun getBalances(): LiveData<List<Balance>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBalances(balances: List<Balance>)
}