package com.tiendasuplementos.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("authToken") val token: String,
    @SerializedName("role") val role: String,
    @SerializedName("status") val status: String?,
    @SerializedName("id") val userId: Int // Campo añadido para el ID del usuario
)
