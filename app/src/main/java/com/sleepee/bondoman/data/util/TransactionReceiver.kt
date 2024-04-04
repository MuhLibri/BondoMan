package com.sleepee.bondoman.data.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.sleepee.bondoman.data.model.TransactionDao
import com.sleepee.bondoman.data.model.TransactionDatabase
import kotlin.concurrent.thread

class TransactionReceiver : BroadcastReceiver() {
    private lateinit var database: TransactionDatabase
    private val transactionDao: TransactionDao by lazy {
        database.getTransactionDao()
    }
    override fun onReceive(context: Context, intent: Intent) {
        database = TransactionDatabase.getDatabase(context)
        val formattedDate = TransactionUtils.getCurrentDate()
        val title = intent.getStringExtra("title")
        val amount = intent.getIntExtra("amount", 0)
        val location = intent.getStringExtra("location")
        val category = intent.getStringExtra("category")

        val transaction = TransactionUtils.convertToTransaction(title.toString(), amount, formattedDate, location.toString(), category.toString())
        thread {
            transactionDao.createTransaction(transaction)
        }
    }
}