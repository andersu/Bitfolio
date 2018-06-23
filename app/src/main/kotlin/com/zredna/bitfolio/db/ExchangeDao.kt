package com.zredna.bitfolio.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.zredna.bitfolio.db.datamodel.Exchange

@Dao
interface ExchangeDao {

    @Query("SELECT * FROM exchanges")
    fun getExchanges(): LiveData<List<Exchange>>

    @Insert
    fun insert(exchange: Exchange)
}