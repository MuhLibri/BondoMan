package com.sleepee.bondoman.data.util

import android.widget.EditText
import com.sleepee.bondoman.data.model.Transaction
import java.net.URLEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TransactionUtils {

    fun convertToTransaction(
        title: String,
        amount: Int,
        formattedDate: String?,
        location: String,
        category: String
    ): Transaction {
        val transaction = Transaction(
            title = title,
            amount = amount,
            category = category,
            date = formattedDate.toString(),
            location = location,
            locationLink = "geo:0,0?q=${URLEncoder.encode(location, "UTF-8")}"
        )
        return transaction
    }

    fun getCurrentDate(): String? {
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return current.format(formatter)
    }
}