package com.zredna.bitfolio.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.zredna.bitfolio.db.datamodel.BalanceInBtc

@Dao
interface BalanceDao {

    @Query("SELECT * FROM balances")
    fun getBalances(): LiveData<List<BalanceInBtc>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBalances(balances: List<BalanceInBtc>)
}