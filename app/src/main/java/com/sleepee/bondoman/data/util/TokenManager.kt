package com.sleepee.bondoman.data.util

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREF_NAME = "com.sleepee.bondoman.prefs"
    private lateinit var sharedPref : SharedPreferences

    const val JWT_EXPIRED = "com.sleepee.bondoman.JWT_EXPIRED"

    fun init(context: Context) {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isTokenStored() : Boolean {
        return sharedPref.contains("token")
    }

    fun storeToken(token: String) {
        with (sharedPref.edit()) {
            putString("token", token)
            apply()
        }
    }

    fun getToken() : String? {
        return sharedPref.getString("token", null)
    }

    fun clearToken() {
        with (sharedPref.edit()) {
            remove("token")
            apply()
        }
    }
}