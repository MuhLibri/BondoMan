package com.sleepee.bondoman.presentation.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.sleepee.bondoman.R

class ToOnlineDialogFragment : DialogFragment() {
    private lateinit var listener: ToOnlineDialogListener

    interface ToOnlineDialogListener {
        fun onOkClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ToOnlineDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement ConnectivityDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = it.layoutInflater
            val dialogView = inflater.inflate(R.layout.fragment_go_online, null)

            val dialog = AlertDialog.Builder(it).create()

            val tryAgainBtn = dialogView.findViewById<Button>(R.id.okBtn)
            tryAgainBtn.setOnClickListener {
                listener.onOkClick(this)
            }

            dialog.setView(dialogView)
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}