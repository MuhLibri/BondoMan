package com.sleepee.bondoman.data.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class TransactionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        println(intent.action.toString())
        println(intent.getStringExtra("title"))
    }
}