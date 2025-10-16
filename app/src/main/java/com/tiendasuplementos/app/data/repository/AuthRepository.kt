package com.tiendasuplementos.app.data.repository

import com.tiendasuplementos.app.data.remote.RetrofitClient
import com.tiendasuplementos.app.data.remote.dto.AuthRequest
import com.tiendasuplementos.app.data.remote.dto.AuthResponse
import com.tiendasuplementos.app.data.remote.dto.User
import retrofit2.Response

class AuthRepository {

    private val authApiService = RetrofitClient.authApiService

    suspend fun loginUser(authRequest: AuthRequest): Response<AuthResponse> {
        return authApiService.login(authRequest)
    }

    suspend fun getProfile(token: String): Response<User> {
        return authApiService.getProfile("Bearer $token")
    }
}