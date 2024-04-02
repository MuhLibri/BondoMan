package com.sleepee.bondoman.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sleepee.bondoman.data.util.TokenManager
import com.sleepee.bondoman.databinding.FragmentSettingsBinding
import com.sleepee.bondoman.presentation.activity.LoginActivity
import com.sleepee.bondoman.presentation.activity.MainActivity
import kotlin.random.Random

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
            Intent().also { intent ->

                val titles = arrayOf("Jalan-Jalan", "Beli Geprek", "Beli Rendang", "Beli Minecraft", "Beli Sayuran", "Donasi", "Honor", "Gajian Cuy", "Jackpot", "Menang Give Away", "Gacor Kang")
                val tidx = Random.nextInt(titles.size)
                val pidx = 5
                val selectedTitle = titles[tidx]
                val category = if (tidx <= pidx) "Pengeluaran" else "Pemasukan"
                val amount = Random.nextInt(1000, 100001)
                val locations = arrayOf("Cisistu", "Sangkuriang", "Dago", "Lembang", "ITB", "Jatinangor")
                val lidx = Random.nextInt(locations.size)
                val selectedLocation = locations[lidx]

                intent.setAction("com.sleepee.bondoman.addTransaction")
                intent.putExtra("title", selectedTitle)
                intent.putExtra("amount", amount)
                intent.putExtra("category", category)
                intent.putExtra("location", selectedLocation)
                context?.sendBroadcast(intent)
            }
            Toast.makeText(requireContext(), "Randomized transaction has been broadcasted", Toast.LENGTH_SHORT).show()
        }

        keluarButton.setOnClickListener{
            TokenManager.clearToken(requireContext())
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }
}