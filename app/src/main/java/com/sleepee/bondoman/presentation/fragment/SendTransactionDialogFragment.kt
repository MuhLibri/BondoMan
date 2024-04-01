package com.sleepee.bondoman.presentation.fragment

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.sleepee.bondoman.R
import com.sleepee.bondoman.data.model.Transaction
import com.sleepee.bondoman.data.util.CredentialManager
import com.sleepee.bondoman.data.util.TokenManager
import com.sleepee.bondoman.data.util.TransactionUtils
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream


class SendTransactionDialogFragment : DialogFragment() {
    private lateinit var email : String
    private val fileName = "Attachment"
    private var format = ".xlsx"
    private val sendVal = 215

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_send_transaction_dialog, null)
        val sendButton = dialogView.findViewById<Button>(R.id.sendButton)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup)
        radioGroup.check(R.id.xlsxOption)

        sendButton.setOnClickListener {
            val selectedOption = radioGroup.checkedRadioButtonId
            format = dialogView.findViewById<RadioButton>(selectedOption).text.toString()

            val sh = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            val transactions = sh.getString("transactions", "").toString()
            val gson = Gson()
            val transactionArray = gson.fromJson(transactions, Array<Transaction>::class.java)

            try {
                TransactionUtils.saveTransaction(fileName, format, transactionArray)
                this.sendEmail()
            }  catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to send email", Toast.LENGTH_SHORT).show()
            }
        }

        return AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
    }

    private fun sendEmail() {
        val subject = "Riwayat Transaksi"
        val message = "Berikut adalah riwayat transaksi anda"
        val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/" + fileName + format)
        val author = context?.packageName
        val uri = context?.let { FileProvider.getUriForFile(it, "$author.provider", file) }
        email = CredentialManager.getEmail(requireContext()).toString()

        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "*/*"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivityForResult(intent, sendVal)
            Toast.makeText(requireContext(), "Email sent to $email", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Failed to send email", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/" + fileName + format)

        if (requestCode == sendVal && resultCode == RESULT_OK) {
            Toast.makeText(requireContext(), "Email sent to $email", Toast.LENGTH_SHORT).show()
            file.delete()
        }
    }
}