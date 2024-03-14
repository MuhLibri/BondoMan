package com.sleepee.bondoman.network.api

import com.sleepee.bondoman.data.model.LoginRequest
import com.sleepee.bondoman.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

public interface LoginApiService {
    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}
