package com.zredna.bitfolio.db.datamodel

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "exchanges")
data class Exchange(
        @PrimaryKey
        val name: String
)