package com.sleepee.bondoman.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.sleepee.bondoman.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var saveDialog: SaveTransactionDialogFragment
    private lateinit var sendDialog: SendTransactionDialogFragment
    private lateinit var simpanButton: Button
    private lateinit var kirimButton: Button
    private lateinit var acakButton: Button
    private lateinit var keluarButton: Button

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
        saveDialog = SaveTransactionDialogFragment()
        sendDialog = SendTransactionDialogFragment()

        simpanButton = binding.simpanButton
        kirimButton = binding.kirimButton
        acakButton = binding.acakButton
        keluarButton = binding.keluarButton

        simpanButton.setOnClickListener {
            saveDialog.show(childFragmentManager, "Save")
        }

        kirimButton.setOnClickListener {
            sendDialog.show(childFragmentManager, "Send")
        }

        acakButton.setOnClickListener {
            // TO DO Implement
        }

        keluarButton.setOnClickListener{
            // TO DO Implement
        }
    }
}