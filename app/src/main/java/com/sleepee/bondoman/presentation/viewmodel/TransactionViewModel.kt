package com.sleepee.bondoman.presentation.viewmodel

import android.app.Application
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.sleepee.bondoman.data.model.Transaction
import com.sleepee.bondoman.data.model.TransactionDatabase
import com.sleepee.bondoman.data.model.TransactionRepository

class TransactionViewModel(application: Application): AndroidViewModel(application = application) {
    val readAllData: LiveData<List<Transaction>>
    private var repository: TransactionRepository

    init {
        val transactionDao = TransactionDatabase.getDatabase(application).getTransactionDao()
        repository = TransactionRepository(transactionDao)
        readAllData = repository.readAllData
    }

    public val pemasukanCount: LiveData<Int>
        get() = repository.getPemasukanCount()

    public val pengeluaranCount: LiveData<Int>
        get() = repository.getPengeluaranCount()

}