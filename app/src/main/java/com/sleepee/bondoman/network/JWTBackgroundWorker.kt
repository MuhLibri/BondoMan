package com.sleepee.bondoman.network

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.sleepee.bondoman.data.util.TokenManager
import com.sleepee.bondoman.network.api.JWTApiService
import com.sleepee.bondoman.network.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class JWTBackgroundWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {
    private val jwtService : JWTApiService = RetrofitClient.Instance.create(JWTApiService::class.java)
    override suspend fun doWork(): Result {
        TokenManager.init(applicationContext)

        // For demo purposes only
//        TokenManager.clearToken()
//        broadcastTokenExpired()
//        return Result.success()

        val res = withContext(Dispatchers.IO) {
            val tokenHeader : String = "Bearer ${TokenManager.getToken()}"
            try {
                jwtService.checkToken(tokenHeader)
            } catch (e: Exception) {
                Log.d("JWTBackgroundWorker", "Error checking token: ${e.message}")
                null
            }
        }

        Log.d("JWTBackgroundWorker", "Result: $res")

        return if (res != null) {
            if (res.isSuccessful) {
                Log.d("JWTBackgroundWorker", "Token is still valid")
                enqueueNextWorker()
            } else {
                Log.d("JWTBackgroundWorker", "Token is invalid, logging out")
                TokenManager.clearToken()

                broadcastTokenExpired()
            }
            Result.success()
        } else {
            retryWithoutDelay()
            Result.success()
        }
    }

    private fun broadcastTokenExpired() {
        val broadcastIntent = Intent(TokenManager.JWT_EXPIRED)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(broadcastIntent)
    }

    private fun displayTokenExpiredToast() {
        Toast.makeText(applicationContext, "Token expired, logging out", Toast.LENGTH_SHORT)
            .show()
    }

    private fun retryWithoutDelay() {
        val nextWorkRequest = OneTimeWorkRequestBuilder<JWTBackgroundWorker>()
            .build()

        WorkManager.getInstance(applicationContext).enqueue(nextWorkRequest)
    }


    private fun enqueueNextWorker() {
        val nextWorkRequest = OneTimeWorkRequestBuilder<JWTBackgroundWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(nextWorkRequest)
    }
}