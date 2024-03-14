package com.sleepee.bondoman.network

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sleepee.bondoman.data.util.TokenManager
import com.sleepee.bondoman.network.api.JWTApiService
import com.sleepee.bondoman.network.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class JWTBackgroundService (
    private val appCtx : Context,
    private val params: WorkerParameters
) : CoroutineWorker(appCtx, params) {
    private val jwtService : JWTApiService = RetrofitClient.Instance.create(JWTApiService::class.java)
    private val token = "Bearer ${TokenManager.getToken()}"
    override suspend fun doWork(): Result {
//        return withContext(Dispatchers.IO) {
//            try {
//                val response = jwtService.checkToken(token)
//                if (!response.isSuccessful) {
//                    handleTokenExpiration()
//                    Result.failure()
//                } else {
//                    val body = response.body()
//                    Log.d("JWTBackgroundService", "Token active! expiration: ${body?.exp}")
//                }
//                Result.success()
//            } catch (e: Exception) {
//                Log.e("JWTBackgroundService", "Error checking token", e)
//                Result.retry()
//            }
//        }

        // Debugging purposes
//        Log.d("JWTBackgroundService", "Checking token...")
//        delay(2000)
//        handleTokenExpiration()
        return Result.success()
    }

    private fun handleTokenExpiration() {
        Log.d("JWTBackgroundService", "Token expired, clearing token")
        TokenManager.clearToken()

        Log.d("JWTBackgroundService", "Sending broadcast")
        val jwtExpirationIntent = Intent(TokenManager.JWT_EXPIRED)
        LocalBroadcastManager.getInstance(appCtx).sendBroadcast(jwtExpirationIntent)
    }
}