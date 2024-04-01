package com.sleepee.bondoman.presentation.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.sleepee.bondoman.data.model.LoginRequest
import com.sleepee.bondoman.data.util.TokenManager
import com.sleepee.bondoman.network.api.LoginApiService
import com.sleepee.bondoman.network.api.RetrofitClient
import com.sleepee.bondoman.databinding.ActivityLoginBinding
import com.sleepee.bondoman.network.JWTBackgroundWorker
import com.sleepee.bondoman.network.NetworkUtils
import com.sleepee.bondoman.presentation.fragment.NoConnectivityDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity(), NoConnectivityDialogFragment.ConnectivityDialogListener {
    private lateinit var binding: ActivityLoginBinding
    private val loginService : LoginApiService = RetrofitClient.Instance.create(LoginApiService::class.java)
    private lateinit var mainActivityIntent : Intent
    private lateinit var connDialog : NoConnectivityDialogFragment
    private lateinit var workManager : WorkManager

    @SuppressLint("SetTextI18n") // delete this later
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(applicationContext)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        mainActivityIntent = Intent(this, MainActivity::class.java)
        workManager = WorkManager.getInstance(applicationContext)

        setContentView(binding.root)

        val loginButton : Button = binding.loginButton

        val emailEditText : EditText = binding.emailInput
        val passwordEditText : EditText = binding.passwordInput

        // For ease of testing only
        emailEditText.setText("13521085@std.stei.itb.ac.id")
        passwordEditText.setText("password_13521085")

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            login(email, password)
        }

        connDialog = NoConnectivityDialogFragment()
        val isNetworkConnected = NetworkUtils.isConnected(this)
        if (!isNetworkConnected) {
            showConnDialog()
        }

        Log.d("LoginActivity", "Token stored: ${TokenManager.isTokenStored()}")

        if (TokenManager.isTokenStored()) {
            NetworkUtils.appConnected = true
            startActivity(mainActivityIntent)
            return
        }
    }

    private fun showConnDialog() {
        connDialog.show(supportFragmentManager, "ConnectivityDialogFragment")
    }

    override fun onTryConnectivityAgainClick(dialog: DialogFragment) {
        dialog.dismiss()
    }
    override fun onAccessAppOfflineClick(dialog: DialogFragment) {
        dialog.dismiss()
        NetworkUtils.appConnected = false
        startActivity(mainActivityIntent)
    }

    private fun startJWTBackgroundService() {
        Log.d("LoginActivity", "JWTBackgroundService started")
        val workRequest = OneTimeWorkRequestBuilder<JWTBackgroundWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest);
    }

    private fun login(email : String, password : String) {
        // Validate email and password
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill the email and password field", Toast.LENGTH_SHORT).show()
            return
        }

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches().not()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return
        }

        val isNetworkConnected = NetworkUtils.isConnected(this)
        if (!isNetworkConnected) {
            showConnDialog()
            return
        }

        lifecycleScope.launch {
            val res = withContext(Dispatchers.IO) {
                try {
                    loginService.login(LoginRequest(email, password))
                } catch (e: Exception) {
                    Log.e("LoginActivity", "Login failed: ${e.message}")
                    null
                }
            }

            if (res != null && res.isSuccessful && res.body() != null){
                val token = res.body()!!.token
                NetworkUtils.encrypt(token)?.let { TokenManager.storeToken(it) }
                Log.d("LoginActivity", "Login success with token $token")

                startJWTBackgroundService()
                NetworkUtils.appConnected = true
                startActivity(mainActivityIntent)
            } else {
                Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}