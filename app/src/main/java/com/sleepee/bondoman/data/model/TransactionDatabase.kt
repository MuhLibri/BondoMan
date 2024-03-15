package com.sleepee.bondoman.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Transaction::class], version = 1)
abstract class TransactionDatabase : RoomDatabase() {

    abstract fun getTransactionDao() : TransactionDao

    companion object {
        fun createDatabase(context: Context): TransactionDatabase {
            return Room.databaseBuilder(
                context,
                TransactionDatabase::class.java,
                "transaction-database"
            ).build()
        }
    }

}

