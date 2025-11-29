package com.tiendasuplementos.app.data.remote.api

import com.tiendasuplementos.app.data.remote.dto.UpdateStatusRequest
import com.tiendasuplementos.app.data.remote.dto.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {

    // Corregido: Se apunta al endpoint singular /user que existe en el StoreAPI
    @GET("user")
    suspend fun getUsers(@Query("name") query: String?): Response<List<User>>

    @PATCH("user/{user_id}")
    suspend fun toggleUserStatus(@Path("user_id") userId: Int, @Body request: UpdateStatusRequest): Response<Unit>
}