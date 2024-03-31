package com.sleepee.bondoman.presentation.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.sleepee.bondoman.R
import com.sleepee.bondoman.data.model.Transaction
import com.sleepee.bondoman.data.util.TransactionUtils


class SaveTransactionDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_format_dialog, null)
        val saveButton = dialogView.findViewById<Button>(R.id.saveButton)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup)
        radioGroup.check(R.id.xlsxOption)

        saveButton.setOnClickListener {
            val selectedOption = radioGroup.checkedRadioButtonId
            val format = dialogView.findViewById<RadioButton>(selectedOption).text.toString()
            val fileName = "Transaction"

            val sh = requireActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE)
            val transactions = sh.getString("transactions", "").toString()
            val gson = Gson()
            val transactionArray = gson.fromJson(transactions, Array<Transaction>::class.java)

            try {
                TransactionUtils.saveTransaction(fileName, format, transactionArray)
                Toast.makeText(requireContext(), "Transaction Saved to $fileName$format", Toast.LENGTH_SHORT).show()
            }  catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to save transaction", Toast.LENGTH_SHORT).show()
            }
        }

        return AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
    }
}