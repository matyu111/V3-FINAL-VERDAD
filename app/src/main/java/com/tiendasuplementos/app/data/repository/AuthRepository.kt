package com.tiendasuplementos.app.data.repository

import android.content.Context
import com.tiendasuplementos.app.data.remote.RetrofitClient
import com.tiendasuplementos.app.data.remote.dto.AuthRequest
import com.tiendasuplementos.app.data.remote.dto.AuthResponse
import com.tiendasuplementos.app.data.remote.dto.UpdateProfileRequest
import com.tiendasuplementos.app.data.remote.dto.User
import retrofit2.Response

// Corregido: Se añade el contexto para obtener el servicio de la API
class AuthRepository(context: Context) {

    // Corregido: Se obtiene el servicio a través de la nueva función
    private val authApiService = RetrofitClient.getAuthApiService(context)

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