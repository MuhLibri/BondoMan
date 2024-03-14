package com.sleepee.bondoman.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.commit
import com.google.android.material.navigation.NavigationBarView
import com.sleepee.bondoman.R
import com.sleepee.bondoman.databinding.ActivityMainBinding
import com.sleepee.bondoman.presentation.fragment.GraphFragment
import com.sleepee.bondoman.presentation.fragment.ScanFragment
import com.sleepee.bondoman.presentation.fragment.SettingsFragment
import com.sleepee.bondoman.presentation.fragment.TransactionFragment

class MainActivity : BaseActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.bottomNav.setOnItemSelectedListener(this)

        supportFragmentManager.commit {
            add(R.id.frame_content, TransactionFragment())
        }

        // Handle back button => Minimize the app, no logout
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                moveTaskToBack(true)
            }
        })

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_transaction -> {
                supportFragmentManager.commit {
                    replace(R.id.frame_content, TransactionFragment())
                }
                true
            }
            R.id.nav_scan -> {
                supportFragmentManager.commit {
                    replace(R.id.frame_content, ScanFragment())
                }
                true
            }
            R.id.nav_graph -> {
                supportFragmentManager.commit {
                    replace(R.id.frame_content, GraphFragment())
                }
                true
            }
            R.id.nav_settings -> {
                supportFragmentManager.commit {
                    replace(R.id.frame_content, SettingsFragment())
                }
                true
            }
            else -> {
                false
            }
        }
    }
}