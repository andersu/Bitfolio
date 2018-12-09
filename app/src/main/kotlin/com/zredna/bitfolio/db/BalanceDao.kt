package com.zredna.bitfolio.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zredna.bitfolio.db.datamodel.BalanceInBtc

@Dao
interface BalanceDao {

    @Query("SELECT * FROM balances")
    fun getBalances(): LiveData<List<BalanceInBtc>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBalances(balances: List<BalanceInBtc>)

    @Query("DELETE FROM balances")
    fun deleteBalances()
}