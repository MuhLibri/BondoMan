package com.sleepee.bondoman.presentation.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.WorkManager
import com.sleepee.bondoman.data.util.TokenManager
import com.sleepee.bondoman.network.JWTBackgroundWorker
import com.sleepee.bondoman.network.NetworkUtils
import com.sleepee.bondoman.presentation.fragment.ToOfflineDialogFragment
import com.sleepee.bondoman.presentation.fragment.ToOnlineDialogFragment

abstract class BaseActivity :
    AppCompatActivity(),
    ToOnlineDialogFragment.ToOnlineDialogListener,
    ToOfflineDialogFragment.ToOfflineDialogListener
{
    private lateinit var toOnlineDialog : ToOnlineDialogFragment
    private lateinit var toOfflineDialog : ToOfflineDialogFragment

    private val jwtExpirationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == TokenManager.JWT_EXPIRED) {
                Log.d("BaseActivity", "JWT expired, logging out")
                finish()
            }
        }
    }

    private val connectivityChangeReceiver = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: android.net.Network) {
            super.onAvailable(network)
            if (!NetworkUtils.appConnected && !toOnlineDialog.isAdded)
                toOnlineDialog.show(supportFragmentManager, "toOnlineDialog")
        }

        override fun onLost(network: android.net.Network) {
            super.onLost(network)
            if (NetworkUtils.appConnected && !toOfflineDialog.isAdded)
                toOfflineDialog.show(supportFragmentManager, "toOfflineDialog")
        }
    }

    private fun registerConnectivityChangeReceiver() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder().
            addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).
            build()
        connectivityManager.registerNetworkCallback(networkRequest, connectivityChangeReceiver)
    }

    private fun unregisterConnectivityChangeReceiver() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(connectivityChangeReceiver)
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        TokenManager.init(applicationContext)

        LocalBroadcastManager.getInstance(applicationContext)
            .registerReceiver(jwtExpirationReceiver, IntentFilter(TokenManager.JWT_EXPIRED))
        registerConnectivityChangeReceiver()

        toOnlineDialog = ToOnlineDialogFragment()
        toOfflineDialog = ToOfflineDialogFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(applicationContext)
            .unregisterReceiver(jwtExpirationReceiver)
        unregisterConnectivityChangeReceiver()
    }

    override fun onToOnlineClick(dialog: DialogFragment) {
        dialog.dismiss()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // go to login activity
    }

    override fun onToOfflineClick(dialog: DialogFragment) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // go to login activity
    }
}
