package com.sleepee.bondoman.presentation.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.sleepee.bondoman.R

class SaveTransactionDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_format_dialog, null)

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }
}