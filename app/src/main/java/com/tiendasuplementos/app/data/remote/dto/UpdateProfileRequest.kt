package com.tiendasuplementos.app.data.remote.dto

import com.google.gson.annotations.SerializedName

// Corregido: Ahora acepta nombre y una contraseña opcional
data class UpdateProfileRequest(
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String?
)
