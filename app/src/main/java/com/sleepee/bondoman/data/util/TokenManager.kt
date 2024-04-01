package com.sleepee.bondoman.data.util

import android.content.Context
import android.content.SharedPreferences
import com.sleepee.bondoman.network.NetworkUtils

object TokenManager {
    private const val PREF_NAME = "com.sleepee.bondoman.prefs"
    private lateinit var sharedPref : SharedPreferences

    const val JWT_EXPIRED = "com.sleepee.bondoman.JWT_EXPIRED"
    private const val KEY_TOKEN = "token"

    fun isTokenStored(context: Context) : Boolean {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.contains(KEY_TOKEN)
    }

    fun storeToken(context: Context, token: String) {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString(KEY_TOKEN, token)
            apply()
        }
    }

    fun getToken(context: Context) : String? {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_TOKEN, null)?.let { NetworkUtils.decrypt(it) }
    }

    fun clearToken(context: Context) {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        if (!isTokenStored(context)) return
        with (sharedPref.edit()) {
            remove(KEY_TOKEN)
            apply()
        }
    }
}