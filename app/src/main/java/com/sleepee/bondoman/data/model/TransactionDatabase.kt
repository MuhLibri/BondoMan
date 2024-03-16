package com.sleepee.bondoman.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Transaction::class], version = 1)
abstract class TransactionDatabase : RoomDatabase() {

    abstract fun getTransactionDao() : TransactionDao

    companion object {

        @Volatile
        private var DATABASE_INSTANCE: TransactionDatabase ? = null
        fun getDatabase(context: Context): TransactionDatabase {

            return DATABASE_INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDatabase::class.java,
                    "transaction-database"
                ).build()
                DATABASE_INSTANCE = instance
                return instance
            }
        }
    }

}

