package com.sleepee.bondoman.presentation.activity

import android.R
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import com.sleepee.bondoman.databinding.ActivityAddTransactionBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val INTENT_EXTRA_LOCATION = "location"
const val INTENT_EXTRA_LATITUDE = "latitude"
const val INTENT_EXTRA_LONGITUDE = "longitude"


class AddTransactionActivity: BaseActivity() {

    private lateinit var binding: ActivityAddTransactionBinding
    var selectedItem: String = "Pemasukan"
    private var locationString: String ?= null
    private var currentLatitude : Double ?= null
    private var currentLongitude : Double ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get location + coordinates data from the TransactionFragment
        locationString = intent.getStringExtra(INTENT_EXTRA_LOCATION)
        currentLatitude = intent.getDoubleExtra(INTENT_EXTRA_LATITUDE, -6.890563)
        currentLongitude = intent.getDoubleExtra(INTENT_EXTRA_LONGITUDE, 107.610696)


        setupUI()
    }

    private fun setupUI() {
        title = "Add New Transaction"
        setupCategoryDropdown()
        setupBackButton()
        binding.location.setText(locationString)
        binding.addTransactionButton.setOnClickListener {
            val title = binding.title
            val amount = binding.amount
            val location = binding.location

            validateAddActivity(title, amount, location)
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
                finish()
            }
            .setNegativeButton("No", null)
            .show()
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
            amount.error = "Amount must not be 0/amount is invalid"
            return
        }

        val formatted = getCurrentDate()

        Toast.makeText(this, "$formatted, $currentLatitude, $currentLongitude", Toast.LENGTH_SHORT).show()
    }

    private fun getCurrentDate(): String? {
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return current.format(formatter)
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
