package com.sleepee.bondoman.network.api

import com.sleepee.bondoman.data.model.TokenExpirationResponse
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface JWTApiService {
    @POST("api/auth/token")
    suspend fun checkToken(@Header("Authorization") token: String): Response<TokenExpirationResponse>
}