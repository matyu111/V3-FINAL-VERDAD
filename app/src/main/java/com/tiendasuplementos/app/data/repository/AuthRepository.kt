package com.tiendasuplementos.app.data.repository

import com.tiendasuplementos.app.data.remote.RetrofitClient
import com.tiendasuplementos.app.data.remote.dto.AuthRequest
import com.tiendasuplementos.app.data.remote.dto.AuthResponse
import retrofit2.Response

class AuthRepository {

    // Obtenemos la instancia del servicio de API desde nuestro RetrofitClient
    private val authApiService = RetrofitClient.authApiService

    // Función suspendida para iniciar sesión, que simplemente llama al método de la API
    suspend fun loginUser(authRequest: AuthRequest): Response<AuthResponse> {
        return authApiService.login(authRequest)
    }
}