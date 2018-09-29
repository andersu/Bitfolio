package com.zredna.bitfolio.db.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchanges")
data class Exchange(
        @PrimaryKey
        val name: String
)