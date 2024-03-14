package com.sleepee.bondoman.presentation.activity

import android.R
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sleepee.bondoman.databinding.ActivityAddTransactionBinding


class AddTransactionActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAddTransactionBinding
    var selectedItem: String = "Pemasukan"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        title = "Add New Transaction"
        setupCategoryDropdown()
        binding.addTransactionButton.setOnClickListener {
            val title = binding.title
            val amount = binding.amount
            val location = binding.location

            validateAddActivity(title, amount, location)
        }

    }

    private fun validateAddActivity(title: EditText, amount: EditText, location: EditText) {
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
            amount.error = "Amount must not be 0"
            return
        }

        Toast.makeText(this, "Okay", Toast.LENGTH_SHORT).show()
    }

    private fun setupCategoryDropdown() {
        val category = arrayOf("Pemasukan", "Pengeluaran")
        val spinner = binding.categorySpinner
        val arrayAdapter =
            ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, category)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedItem = parent?.getItemAtPosition(position).toString()
                Toast.makeText(
                    this@AddTransactionActivity,
                    "this is $selectedItem",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }
}