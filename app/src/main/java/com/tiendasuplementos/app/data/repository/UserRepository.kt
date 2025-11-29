package com.tiendasuplementos.app.data.repository

import android.content.Context
import com.tiendasuplementos.app.data.remote.RetrofitClient
import com.tiendasuplementos.app.data.remote.dto.UpdateStatusRequest
import com.tiendasuplementos.app.data.remote.dto.User
import retrofit2.Response

class UserRepository(context: Context) {

    private val userApiService = RetrofitClient.getUserApiService(context)

    suspend fun getUsers(query: String?): Response<List<User>> {
        return userApiService.getUsers(query)
    }

    // Corregido: La función ahora acepta el objeto User completo para construir la petición.
    suspend fun toggleUserStatus(user: User, newStatus: String): Response<Unit> {
        val request = UpdateStatusRequest(
            name = user.name,
            email = user.email,
            status = newStatus
        )
        return userApiService.toggleUserStatus(user.id, request)
    }
}