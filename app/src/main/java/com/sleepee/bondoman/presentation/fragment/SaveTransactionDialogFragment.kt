package com.sleepee.bondoman.presentation.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.sleepee.bondoman.R
import com.sleepee.bondoman.presentation.adapter.TransactionsAdapter
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream


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
            this.saveTransaction(fileName, format)
        }

        return AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
    }

    private fun saveTransaction(fileName: String, format: String) {
        try {
            val workbook = if (format == ".xls") HSSFWorkbook() else XSSFWorkbook()
            val workSheet = workbook.createSheet()
            val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/" + fileName + format)

            // Header Font
            val headerFont: Font = workbook.createFont()
            headerFont.color = IndexedColors.WHITE.index

            // Header Style
            val headerStyle = workbook.createCellStyle()
            headerStyle.alignment = HorizontalAlignment.CENTER
            headerStyle.fillForegroundColor = IndexedColors.VIOLET.index
            headerStyle.fillPattern = FillPatternType.SOLID_FOREGROUND
            headerStyle.setFont(headerFont)

            // Header
            val row0 = workSheet.createRow(0)
            // Col 0
            var cell0 = row0.createCell(0)
            cell0.setCellValue("Date")
            cell0.cellStyle = headerStyle
            workSheet.setColumnWidth(0, (cell0.stringCellValue.length + 10) * 256)
            // Col 1
            var cell1 = row0.createCell(1)
            cell1.setCellValue("Category")
            cell1.cellStyle = headerStyle
            workSheet.setColumnWidth(1, (cell1.stringCellValue.length + 5) * 256)
            // Col 2
            var cell2 = row0.createCell(2)
            cell2.setCellValue("Amount")
            cell2.cellStyle = headerStyle
            workSheet.setColumnWidth(2, (cell2.stringCellValue.length + 10) * 256)
            // Col 3
            var cell3 = row0.createCell(3)
            cell3.setCellValue("Title")
            cell3.cellStyle = headerStyle
            workSheet.setColumnWidth(3, (cell3.stringCellValue.length + 15) * 256)
            // Col 4
            var cell4 = row0.createCell(4)
            cell4.setCellValue("Location")
            cell4.cellStyle = headerStyle
            workSheet.setColumnWidth(4, (cell4.stringCellValue.length + 25) * 256)

            // Dummy
            // -------------------------------------------------------------------------------------
            val t1 = TransactionModel("27/03/2024", "Pemasukan", 20000, "Kerja", "Jl. Jendral Sudirman No 12, Bandung")
            val data = arrayOf(t1)

            var i = 1
            for (t in data) {
                val row = workSheet.createRow(i)
                i += 1
                row.createCell(0).setCellValue(t.tanggal)
                row.createCell(1).setCellValue(t.kategori)
                row.createCell(2).setCellValue(t.nominal.toString())
                row.createCell(3).setCellValue(t.judul)
                row.createCell(4).setCellValue(t.lokasi)
            }
            // -------------------------------------------------------------------------------------

            val out = FileOutputStream(file)
            workbook.write(out)
            workbook.close()
            Toast.makeText(requireContext(), "Transaction Saved to $fileName$format", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Failed to save transaction", Toast.LENGTH_SHORT).show()
        }
    }
}

class TransactionModel(val tanggal: String, val kategori: String, val nominal: Int, val judul: String, val lokasi: String)
