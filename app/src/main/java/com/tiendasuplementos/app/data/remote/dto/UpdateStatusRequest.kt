package com.tiendasuplementos.app.data.remote.dto

import com.google.gson.annotations.SerializedName

// Corregido: Se añaden los campos que el backend requiere para la edición.
data class UpdateStatusRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("status") val status: String
)
