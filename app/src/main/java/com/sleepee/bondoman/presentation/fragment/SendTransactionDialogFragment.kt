package com.sleepee.bondoman.presentation.fragment

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
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
    private val sendVal = 215

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_send_transaction_dialog, null)
        val sendButton = dialogView.findViewById<Button>(R.id.sendButton)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup)
        radioGroup.check(R.id.xlsxOption)

        sendButton.setOnClickListener {
            val selectedOption = radioGroup.checkedRadioButtonId
            val format = dialogView.findViewById<RadioButton>(selectedOption).text.toString()

            val sh = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            val transactions = sh.getString("transactions", "").toString()
            val gson = Gson()
            val transactionArray = gson.fromJson(transactions, Array<Transaction>::class.java)

            try {
                this.makeAttachment(format, transactionArray)
                this.sendEmail(format)
            }  catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to send email", Toast.LENGTH_SHORT).show()
            }
        }

        return AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
    }

    private fun makeAttachment(format: String, transactions: Array<Transaction>) {
        val cacheDir = context?.cacheDir
        val file = File(cacheDir, fileName + format)
        val workbook = if (format == ".xls") HSSFWorkbook() else XSSFWorkbook()
        val workSheet = workbook.createSheet()

        // Header
        val row0 = workSheet.createRow(0)
        // Col 0
        val cell0 = row0.createCell(0)
        cell0.setCellValue("Date")
        workSheet.setColumnWidth(0, (cell0.stringCellValue.length + 10) * 256)
        // Col 1
        val cell1 = row0.createCell(1)
        cell1.setCellValue("Category")
        workSheet.setColumnWidth(1, (cell1.stringCellValue.length + 5) * 256)
        // Col 2
        val cell2 = row0.createCell(2)
        cell2.setCellValue("Amount")
        workSheet.setColumnWidth(2, (cell2.stringCellValue.length + 10) * 256)
        // Col 3
        val cell3 = row0.createCell(3)
        cell3.setCellValue("Title")
        workSheet.setColumnWidth(3, (cell3.stringCellValue.length + 15) * 256)
        // Col 4
        val cell4 = row0.createCell(4)
        cell4.setCellValue("Location")
        workSheet.setColumnWidth(4, (cell4.stringCellValue.length + 25) * 256)

        if (format == ".xlsx") {
            // Header Font
            val headerFont: Font = workbook.createFont()
            headerFont.color = IndexedColors.WHITE.index

            // Header Style
            val headerStyle = workbook.createCellStyle()
            headerStyle.alignment = HorizontalAlignment.CENTER
            headerStyle.fillForegroundColor = IndexedColors.VIOLET.index
            headerStyle.fillPattern = FillPatternType.SOLID_FOREGROUND
            headerStyle.setFont(headerFont)

            // Setting Header Style
            cell0.cellStyle = headerStyle
            cell1.cellStyle = headerStyle
            cell2.cellStyle = headerStyle
            cell3.cellStyle = headerStyle
            cell4.cellStyle = headerStyle
        }
        // Data
        // -------------------------------------------------------------------------------------
        var i = 1
        for (transaction in transactions) {
            val row = workSheet.createRow(i)
            i += 1
            row.createCell(0).setCellValue(transaction.date)
            row.createCell(1).setCellValue(transaction.category)
            row.createCell(2).setCellValue(transaction.amount.toString())
            row.createCell(3).setCellValue(transaction.title)
            row.createCell(4).setCellValue(transaction.location)
        }
        // -------------------------------------------------------------------------------------

        val out = FileOutputStream(file)
        workbook.write(out)
        workbook.close()
    }

    private fun sendEmail(format: String) {
        val subject = "Riwayat Transaksi"
        val message = "Berikut adalah riwayat transaksi anda"
        val cacheDir = context?.cacheDir
        val file = File(cacheDir, fileName + format)
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

        if (requestCode == sendVal && resultCode == RESULT_OK) {
            Toast.makeText(requireContext(), "Email sent to $email", Toast.LENGTH_SHORT).show()
        }
    }
}