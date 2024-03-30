package com.sleepee.bondoman.network.api

import com.sleepee.bondoman.data.model.ScanResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ScanApiService {
    @Multipart
    @POST("api/bill/upload")
    suspend fun uploadAttachment(@Header("Authorization") token: String, @Part filePart: MultipartBody.Part?): Response<ScanResponse>
}