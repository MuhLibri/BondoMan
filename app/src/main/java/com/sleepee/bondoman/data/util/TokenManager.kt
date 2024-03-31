package com.sleepee.bondoman.data.util

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREF_NAME = "com.sleepee.bondoman.prefs"
    private lateinit var sharedPref : SharedPreferences

    const val JWT_EXPIRED = "com.sleepee.bondoman.JWT_EXPIRED"
    private const val KEY_TOKEN = "token"

    fun init(context: Context) {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isTokenStored() : Boolean {
        return sharedPref.contains(KEY_TOKEN)
    }

    fun storeToken(token: String) {
        with (sharedPref.edit()) {
            putString(KEY_TOKEN, token)
            apply()
        }
    }

    fun getToken() : String? {
        return sharedPref.getString(KEY_TOKEN, null)
    }

    fun clearToken() {
        if (!isTokenStored()) return
        with (sharedPref.edit()) {
            remove(KEY_TOKEN)
            apply()
        }
    }
}