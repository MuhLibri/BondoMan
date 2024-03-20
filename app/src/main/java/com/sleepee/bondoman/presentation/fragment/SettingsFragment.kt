package com.sleepee.bondoman.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.sleepee.bondoman.R
import com.sleepee.bondoman.databinding.FragmentGraphBinding
import com.sleepee.bondoman.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var dialog: SaveTransactionDialogFragment
    private lateinit var simpanButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = SaveTransactionDialogFragment()
        simpanButton = binding.simpanButton

        simpanButton.setOnClickListener {
            dialog.show(childFragmentManager, "tes")
        }
    }
}