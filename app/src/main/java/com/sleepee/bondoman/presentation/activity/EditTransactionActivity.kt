package com.sleepee.bondoman.presentation.activity

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import com.sleepee.bondoman.data.model.Transaction
import com.sleepee.bondoman.data.model.TransactionDao
import com.sleepee.bondoman.data.model.TransactionDatabase
import com.sleepee.bondoman.databinding.ActivityEditTransactionBinding
import java.net.URLEncoder
import kotlin.concurrent.thread

const val EDIT_TRANSACTION_ID = "edit_transaction_id"
const val EDIT_TRANSACTION_TITLE = "edit_transaction_title"
const val EDIT_TRANSACTION_CATEGORY = "edit_transaction_category"
const val EDIT_TRANSACTION_AMOUNT = "edit_transaction_amount"
const val EDIT_TRANSACTION_LOCATION = "edit_transaction_location"
const val EDIT_TRANSACTION_DATE = "edit_transaction_date"
const val EDIT_TRANSACTION_LOCATION_LINK = "edit_transaction_location_link"

class EditTransactionActivity : BaseActivity() {
    private lateinit var database: TransactionDatabase
    private lateinit var binding: ActivityEditTransactionBinding
    private val transactionDao: TransactionDao by lazy {
        database.getTransactionDao()
    }
    private var id = 0
    private lateinit var date: String
    private lateinit var category: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()

        database = TransactionDatabase.getDatabase(this)

    }

    private fun setupUI() {
        title = "Edit Transaction"
        val extras = intent.extras
        setupBackButton()
        getTransactionExtras(extras)
        binding.saveTransactionButton.setOnClickListener {
            val title = binding.titleEditText
            val amount = binding.amountEditText
            val location = binding.locationEditText

            validateEditActivity(title, amount, location)
        }
        binding.deleteTransactionButton.setOnClickListener {
            createDeleteDialog()
        }
    }

    private fun deleteTransaction() {
        val transaction = convertToTransaction(id,
            binding.titleEditText,
            binding.amountEditText,
            binding.locationEditText,
            category,
            date)
        thread {
            transactionDao.deleteTransaction(transaction = transaction)
        }
        Toast.makeText(this, "Transaction deleted successfully!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun validateEditActivity(
        title: EditText,
        amount: EditText,
        location: EditText,
    ) {
        if (title.length() == 0) {
            title.error = "Title cannot be blank"
            return
        }
        if (amount.length() == 0) {
            amount.error = "Amount cannot be blank"
            return
        }
        if (location.length() == 0) {
            location.error = "Location cannot be blank"
            return
        }
        if (!TextUtils.isDigitsOnly(amount.text)) {
            amount.error = "Amount must be an integer"
            return
        }
        if (amount.text.toString()[0] == '0') {
            amount.error = "Amount must not be 0/amount is invalid"
            return
        }

        val transaction = convertToTransaction(id, title, amount, location, category, date)

        thread {
            transactionDao.updateTransaction(transaction = transaction)
        }
        Toast.makeText(this, "Transaction edited successfully!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun convertToTransaction(
        id: Int,
        title: EditText,
        amount: EditText,
        location: EditText,
        category: String,
        date: String,
    ): Transaction {
        val transaction = Transaction(
            transactionId = id,
            title = title.text.toString(),
            amount = amount.text.toString().toInt(),
            category = category,
            date = date,
            location = location.text.toString(),
            locationLink = "geo:0,0?q=${URLEncoder.encode(location.text.toString(), "UTF-8")}"
        )
        return transaction
    }

    @SuppressLint("SetTextI18n")
    private fun getTransactionExtras(extras: Bundle?) {
        if (extras != null) {
            id = extras.getInt(EDIT_TRANSACTION_ID)
            category = extras.getString(EDIT_TRANSACTION_CATEGORY).toString()
            date = extras.getString(EDIT_TRANSACTION_DATE).toString()
            binding.categoryTextview.text =
                "Kategori: $category"
            if (category == "Pemasukan") {
                binding.categoryTextview.setTextColor(Color.parseColor("#87A922"))
            } else {
                binding.categoryTextview.setTextColor(Color.parseColor("#FF004D"))
            }
            binding.dateTextview.text = "Tanggal: $date"
            binding.titleEditText.setText(extras.getString(EDIT_TRANSACTION_TITLE))
            binding.amountEditText.setText(extras.getInt(EDIT_TRANSACTION_AMOUNT).toString())
            binding.locationEditText.setText(extras.getString(EDIT_TRANSACTION_LOCATION))
        }
    }

    private fun setupBackButton() {
        onBackPressedDispatcher.addCallback {
            createConfirmationDialog()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            createConfirmationDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Reset data")
            .setMessage("Are you sure you want to discard all changes?")
            .setPositiveButton("Yes") { _, _ ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun createDeleteDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete transaction")
            .setMessage("Are you sure you want to delete this transaction?")
            .setPositiveButton("Yes") { _, _ ->
                deleteTransaction()
            }
            .setNegativeButton("No", null)
            .show()
    }
}