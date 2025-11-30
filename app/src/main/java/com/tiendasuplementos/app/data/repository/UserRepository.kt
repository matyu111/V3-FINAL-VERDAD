package com.tiendasuplementos.app.data.repository

import com.tiendasuplementos.app.data.remote.RetrofitClient
import com.tiendasuplementos.app.data.remote.dto.UpdateProfileRequest
import com.tiendasuplementos.app.data.remote.dto.UpdateStatusRequest
import com.tiendasuplementos.app.data.remote.dto.User
import retrofit2.Response

class UserRepository {

    private val userApiService = RetrofitClient.userApiService
    private val authApiService = RetrofitClient.authApiService

    suspend fun getUsers(query: String? = null): Response<List<User>> {
        return userApiService.getUsers(query)
    }

    suspend fun updateUser(name: String, password: String? = null): Response<User> {
        val updateRequest = UpdateProfileRequest(name = name, password = password)
        return authApiService.updateMyProfile(updateRequest)
    }

    suspend fun toggleUserStatus(user: User, status: String): Response<Unit> {
        // Map string status to boolean for the API
        // Assuming "blocked" means is_blocked = true
        val isBlocked = status.equals("blocked", ignoreCase = true)
        return userApiService.toggleUserStatus(user.id, UpdateStatusRequest( is_blocked = isBlocked))
    }
}
