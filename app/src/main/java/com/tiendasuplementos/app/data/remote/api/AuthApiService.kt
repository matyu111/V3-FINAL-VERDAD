package com.tiendasuplementos.app.data.remote.api

import com.tiendasuplementos.app.data.remote.dto.AuthRequest
import com.tiendasuplementos.app.data.remote.dto.AuthResponse
import com.tiendasuplementos.app.data.remote.dto.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @GET("auth/me")
    suspend fun getProfile(@Header("Authorization") token: String): Response<User>
}