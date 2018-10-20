package com.zredna.bitfolio.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.zredna.bitfolio.db.datamodel.Exchange

@Dao
interface ExchangeDao {

    @Query("SELECT * FROM exchanges")
    fun getExchanges(): LiveData<List<Exchange>>

    @Insert
    fun insert(exchange: Exchange)

    @Delete
    fun delete(exchange: Exchange)
}