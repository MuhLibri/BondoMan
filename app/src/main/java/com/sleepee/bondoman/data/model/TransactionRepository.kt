package com.sleepee.bondoman.data.model

import androidx.lifecycle.LiveData

class TransactionRepository(private val transactionDao: TransactionDao) {
    val readAllData: LiveData<List<Transaction>> = transactionDao.getAllTransactions()
    public fun getCount(): LiveData<Int> {
        return transactionDao.getTransactionCount()
    }
}