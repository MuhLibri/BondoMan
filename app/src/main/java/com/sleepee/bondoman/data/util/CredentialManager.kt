package com.sleepee.bondoman.data.util

import android.content.Context

object CredentialManager {
    private const val PREF_NAME = "com.sleepee.bondoman.prefs"
    private const val EMAIL_TOKEN = "email"

    fun getEmail(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(EMAIL_TOKEN, null)
    }

    fun storeEmail(context: Context, email: String) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString(EMAIL_TOKEN, email)
            apply()
        }
    }
}