package com.tiendasuplementos.app.data.repository

import com.tiendasuplementos.app.data.remote.RetrofitClient
import com.tiendasuplementos.app.data.remote.dto.AuthRequest
import com.tiendasuplementos.app.data.remote.dto.AuthResponse
import com.tiendasuplementos.app.data.remote.dto.UpdateProfileRequest
import com.tiendasuplementos.app.data.remote.dto.User
import retrofit2.Response

class AuthRepository {

    private val authApiService = RetrofitClient.authApiService

    suspend fun loginUser(authRequest: AuthRequest): Response<AuthResponse> {
        return authApiService.loginUser(authRequest)
    }

    suspend fun getMyProfile(): Response<User> {
        return authApiService.getMyProfile()
    }

    suspend fun updateMyProfile(name: String, password: String?): Response<User> {
        val request = UpdateProfileRequest(name, password)
        return authApiService.updateMyProfile(request)
    }
}