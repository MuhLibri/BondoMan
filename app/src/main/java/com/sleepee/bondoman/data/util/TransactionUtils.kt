package com.sleepee.bondoman.data.util

import android.os.Environment
import android.widget.EditText
import android.widget.Toast
import com.sleepee.bondoman.data.model.Transaction
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream
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

    fun convertToTransactionUsingId(
        id: Int,
        title: String,
        amount: Int,
        location: String,
        category: String,
        date: String,
    ): Transaction {
        val transaction = Transaction(
            transactionId = id,
            title = title,
            amount = amount,
            category = category,
            date = date,
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

    fun saveTransaction(fileName: String, format: String) {
        val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/" + fileName + format)
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
    }
}

class TransactionModel(val tanggal: String, val kategori: String, val nominal: Int, val judul: String, val lokasi: String)