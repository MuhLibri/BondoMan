package com.sleepee.bondoman.data.api

import com.sleepee.bondoman.data.api.model.LoginRequest
import com.sleepee.bondoman.data.api.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

public interface AuthService {
    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}
