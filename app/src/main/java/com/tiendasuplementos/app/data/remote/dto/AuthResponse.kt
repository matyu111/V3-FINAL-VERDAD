package com.tiendasuplementos.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("authToken") // Asegúrate que coincida con el nombre del campo en la respuesta de Xano
    val token: String
)