package com.sleepee.bondoman.presentation.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.sleepee.bondoman.R

class ConnectivityDialogFragment : DialogFragment() {
    private lateinit var listener: ConnectivityDialogListener

    interface ConnectivityDialogListener {
        fun onTryConnectivityAgainClick(dialog: DialogFragment)
        fun onAccessAppOfflineClinck(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ConnectivityDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement ConnectivityDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = it.layoutInflater
            val dialogView = inflater.inflate(R.layout.fragment_connectivity_dialog, null)

            val dialog = AlertDialog.Builder(it).create()

            val tryAgainBtn = dialogView.findViewById<Button>(R.id.tryConnAgainBtn)
            tryAgainBtn.setOnClickListener {
                listener.onTryConnectivityAgainClick(this)
            }

            val accessOfflineBtn = dialogView.findViewById<Button>(R.id.accessOfflineBtn)
            accessOfflineBtn.setOnClickListener {
                listener.onAccessAppOfflineClinck(this)
            }

            dialog.setView(dialogView)
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}