package com.tiendasuplementos.app.data.remote.api

import com.tiendasuplementos.app.data.remote.dto.AuthRequest
import com.tiendasuplementos.app.data.remote.dto.AuthResponse
import com.tiendasuplementos.app.data.remote.dto.UpdateProfileRequest
import com.tiendasuplementos.app.data.remote.dto.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun loginUser(@Body authRequest: AuthRequest): Response<AuthResponse>

    @GET("auth/me")
    suspend fun getMyProfile(): Response<User>

    @PATCH("auth/me")
    suspend fun updateMyProfile(@Body updateRequest: UpdateProfileRequest): Response<User>
}