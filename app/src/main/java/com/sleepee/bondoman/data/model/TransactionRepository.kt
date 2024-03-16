package com.sleepee.bondoman.data.model

class TransactionRepository(private val transactionDao: TransactionDao) {
    val readAllData = transactionDao.getAllTransactions()
}