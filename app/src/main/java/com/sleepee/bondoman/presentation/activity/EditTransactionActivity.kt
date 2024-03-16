package com.sleepee.bondoman.presentation.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sleepee.bondoman.databinding.ActivityEditTransactionBinding

const val EDIT_TRANSACTION_TITLE = "edit_transaction_title"
const val EDIT_TRANSACTION_CATEGORY = "edit_transaction_category"
const val EDIT_TRANSACTION_AMOUNT = "edit_transaction_amount"
const val EDIT_TRANSACTION_LOCATION = "edit_transaction_location"
const val EDIT_TRANSACTION_DATE = "edit_transaction_date"
const val EDIT_TRANSACTION_LOCATION_LINK = "edit_transaction_location_link"

class EditTransactionActivity: BaseActivity() {
    private lateinit var binding: ActivityEditTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras

        setupUI(extras)

    }

    @SuppressLint("SetTextI18n")
    private fun setupUI(extras: Bundle?) {
        if (extras != null) {
            binding.categoryTextview.text = "Kategori: ${extras.getString(EDIT_TRANSACTION_CATEGORY)}"
            if (extras.getString(EDIT_TRANSACTION_CATEGORY) == "Pemasukan") {
                binding.categoryTextview.setTextColor(Color.parseColor("#87A922"))
            } else {
                binding.categoryTextview.setTextColor(Color.parseColor("#FF004D"))
            }
            binding.dateTextview.text = "Tanggal: ${extras.getString(EDIT_TRANSACTION_DATE)}"
            binding.titleEditText.setText(extras.getString(EDIT_TRANSACTION_TITLE))
            binding.amountEditText.setText(extras.getInt(EDIT_TRANSACTION_AMOUNT).toString())
            binding.locationEditText.setText(extras.getString(EDIT_TRANSACTION_LOCATION))
        }
    }
}