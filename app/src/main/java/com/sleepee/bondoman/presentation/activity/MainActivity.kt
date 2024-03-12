package com.sleepee.bondoman.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.commit
import com.sleepee.bondoman.R
import com.sleepee.bondoman.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

//        val logoutButton = binding.logoutButton
//        logoutButton.setOnClickListener {
//            logout()
//        }
//
//        // Handle back button => Minimize the app, no logout
//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                moveTaskToBack(true)
//            }
//        })
        supportFragmentManager.commit {
            add(R.id.frame_content, TransactionFragment())
        }
    }

//    private fun logout() {
//        finish()
//    }
}