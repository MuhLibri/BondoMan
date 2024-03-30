package com.sleepee.bondoman.data.model

import androidx.lifecycle.LiveData
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
    fun getAllTransactions(): LiveData<List<Transaction>>

    @Query("SELECT COUNT(transactionId) FROM `transaction` WHERE category = 'Pemasukan'")
    fun getPemasukanCount(): LiveData<Int>

    @Query("SELECT COUNT(transactionId) FROM `transaction` WHERE category = 'Pengeluaran'")
    fun getPengeluaranCount(): LiveData<Int>

    @Update
    fun updateTransaction(transaction: Transaction)

    @Delete
    fun deleteTransaction(transaction: Transaction)

}