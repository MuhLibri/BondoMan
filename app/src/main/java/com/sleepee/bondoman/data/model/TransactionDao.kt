package com.sleepee.bondoman.data.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransactionDao {

    @Insert
    fun createTransaction(transaction: Transaction)

    @Query("SELECT * FROM `transaction`")
    fun getAllTransactions(): List<Transaction>

    @Update
    fun updateTransaction(transaction: Transaction)

    @Delete
    fun deleteTransaction(transaction: Transaction)

}