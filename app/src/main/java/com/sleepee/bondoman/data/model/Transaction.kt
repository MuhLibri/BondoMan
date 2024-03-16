package com.sleepee.bondoman.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val transactionId: Int = 0,
    val title: String,
    val amount: Int,
    val category: String,
    val location: String,
    val date: String,
    @ColumnInfo(name = "location_link") var locationLink: String? = null
)
