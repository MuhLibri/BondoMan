package com.sleepee.bondoman.presentation.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sleepee.bondoman.data.util.TokenManager

abstract class BaseActivity : AppCompatActivity() {
    private val jwtExpirationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == TokenManager.JWT_EXPIRED) {
                Log.d("BaseActivity", "JWT expired, logging out")
                finish()
            }
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(applicationContext)
            .registerReceiver(jwtExpirationReceiver, IntentFilter(TokenManager.JWT_EXPIRED))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(applicationContext)
            .unregisterReceiver(jwtExpirationReceiver)
    }

    fun stopJwtWorker() {

    }

}
