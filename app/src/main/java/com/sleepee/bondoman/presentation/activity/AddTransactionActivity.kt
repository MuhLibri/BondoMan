package com.sleepee.bondoman.presentation.activity

import android.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
            val title = binding.title.text.toString()
            val amount = binding.amount.text.toString()
            val location = binding.location.text.toString()
        }

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