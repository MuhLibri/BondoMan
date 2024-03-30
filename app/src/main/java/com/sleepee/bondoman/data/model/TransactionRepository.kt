package com.sleepee.bondoman.data.model

import androidx.lifecycle.LiveData

class TransactionRepository(private val transactionDao: TransactionDao) {
    val readAllData: LiveData<List<Transaction>> = transactionDao.getAllTransactions()
    public fun getPemasukanCount(): LiveData<Int> {
        return transactionDao.getPemasukanCount()
    }
    public fun getPengeluaranCount(): LiveData<Int> {
        return transactionDao.getPengeluaranCount()
    }
}